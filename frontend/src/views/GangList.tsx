import { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import "../css/GangView.css";
import { useAlert } from "../context/AlertContext.tsx";
import { useCharacter } from "../context/CharacterContext";

// ----- INTERFACES -----
interface GangInfoDto {
	membersCount: number;
	gangName: string;
	gangDesc: string;
	gangLeader: string;
	gangEmblem: string;
}

interface PageResponse<T> {
	content: T[];
	totalPages: number;
	totalElements: number;
	number: number;
	size: number;
}

// Lista dostępnych emblematów
const EMBLEM_LIST = [
	"gangEmblem1",
	"gangEmblem2",
	"gangEmblem3",
	"gangEmblem4",
];

const GangList = () => {
	const { showError, showInfo } = useAlert();
	const { character } = useCharacter();
	const navigate = useNavigate();

	// ----- STANY LISTY GANGÓW -----
	const [gangs, setGangs] = useState<GangInfoDto[]>([]);
	const [loading, setLoading] = useState<boolean>(false);
	const [page, setPage] = useState<number>(0);
	const [pageSize] = useState<number>(10);
	const [totalPages, setTotalPages] = useState<number>(0);
	const [totalElements, setTotalElements] = useState<number>(0);
	const [joiningGang, setJoiningGang] = useState<string | null>(null);
	const [myGangName, setMyGangName] = useState<string | null>(null);

	// ----- STANY MODALA TWORZENIA GANGU -----
	const [showCreateModal, setShowCreateModal] = useState<boolean>(false);
	const [newGangName, setNewGangName] = useState<string>("");
	const [newGangDesc, setNewGangDesc] = useState<string>("");
	const [selectedEmblem, setSelectedEmblem] = useState<string>(EMBLEM_LIST[0]);
	const [creating, setCreating] = useState<boolean>(false);

	// ----- POBIERANIE NAZWY GANGU POSTACI -----
	const fetchMyGang = useCallback(async () => {
		try {
			const res = await fetch("http://localhost:8080/api/character/gang", {
				credentials: "include",
			});
			if (res.ok) {
				const charData = await res.json();
				setMyGangName(charData.gangName ?? null);
			}
		} catch {
			// cicho
		}
	}, []);

	useEffect(() => {
		fetchMyGang();
	}, [fetchMyGang, character]);

	// ----- POBIERANIE LISTY GANGÓW -----
	const fetchGangs = useCallback(async () => {
		setLoading(true);
		try {
			const params = new URLSearchParams({
				page: page.toString(),
				size: pageSize.toString(),
			});

			const response = await fetch(
				`http://localhost:8080/api/gang?${params.toString()}`,
				{ credentials: "include" },
			);

			if (!response.ok) {
				const errorData = await response.json();
				throw new Error(
					errorData.message || "Nie udało się pobrać listy gangów",
				);
			}

			const data: PageResponse<GangInfoDto> = await response.json();
			setGangs(data.content);
			setTotalPages(data.totalPages);
			setTotalElements(data.totalElements);
		} catch (err: any) {
			showError(err.message || "Błąd połączenia z serwerem");
			setGangs([]);
			setTotalPages(0);
			setTotalElements(0);
		} finally {
			setLoading(false);
		}
	}, [page, pageSize, showError]);

	useEffect(() => {
		fetchGangs();
	}, [fetchGangs]);

	// ----- DOŁĄCZANIE DO GANGU (PROŚBA) -----
	const handleJoinRequest = async (gangName: string) => {
		try {
			setJoiningGang(gangName);
			const res = await fetch(
				`http://localhost:8080/api/gang/${encodeURIComponent(gangName)}/join`,
				{
					method: "POST",
					credentials: "include",
				},
			);

			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Nie udało się wysłać prośby");
			}

			showInfo("Wysłano prośbę o dołączenie!");
		} catch (err: any) {
			showError(err.message);
		} finally {
			setJoiningGang(null);
		}
	};

	// ----- ATAKOWANIE GANGU -----
	const handleAttackGang = async (gangName: string) => {
		try {
			setJoiningGang(gangName);

			const params = new URLSearchParams({
				gangName,
			});

			const res = await fetch(
				`http://localhost:8080/api/gang/startVote?${params.toString()}`,
				{
					method: "POST",
					credentials: "include",
				},
			);

			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Nie udało się rozpocząć głosowania");
			}

			const message = await res.text();
			showInfo(message);
		} catch (err: any) {
			showError(err.message);
		} finally {
			setJoiningGang(null);
		}
	};

	// ----- TWORZENIE GANGU -----
	const handleCreateGang = async (e: React.FormEvent) => {
		e.preventDefault();

		// Walidacja po stronie klienta
		const trimmedName = newGangName.trim();
		const trimmedDesc = newGangDesc.trim();

		if (!trimmedName) {
			showError("Nazwa gangu nie może być pusta.");
			return;
		}
		if (trimmedName.length < 3) {
			showError("Nazwa gangu musi mieć co najmniej 3 znaki.");
			return;
		}
		if (!trimmedDesc) {
			showError("Opis gangu nie może być pusty.");
			return;
		}

		try {
			setCreating(true);

			const params = new URLSearchParams({
				name: trimmedName,
				description: trimmedDesc,
				emblemPath: selectedEmblem,
			});

			const res = await fetch(
				`http://localhost:8080/api/gang/create?${params.toString()}`,
				{
					method: "POST",
					credentials: "include",
				},
			);

			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Nie udało się utworzyć gangu");
			}

			const message = await res.text();
			showInfo(message || "Gang został utworzony!");

			// Zamknij modal i wyczyść formularz
			setShowCreateModal(false);
			setNewGangName("");
			setNewGangDesc("");
			setSelectedEmblem(EMBLEM_LIST[0]);

			// Odśwież dane
			await fetchMyGang();
			await fetchGangs();
		} catch (err: any) {
			showError(err.message);
		} finally {
			setCreating(false);
		}
	};

	// ----- OTWIERANIE MODALA -----
	const openCreateModal = () => {
		setNewGangName("");
		setNewGangDesc("");
		setSelectedEmblem(EMBLEM_LIST[0]);
		setShowCreateModal(true);
	};

	// ----- PAGINACJA -----
	const goToPreviousPage = () => {
		if (page > 0) setPage(page - 1);
	};
	const goToNextPage = () => {
		if (page + 1 < totalPages) setPage(page + 1);
	};
	const getGlobalRank = (index: number) => page * pageSize + index;

	return (
		<div className="gang-container">
			{loading && (
				<div className="gang-loading-overlay">Ładowanie gangów...</div>
			)}

			{/* ----- NAGŁÓWEK ----- */}
			<div className="gang-header-section">
				<h2 className="gang-title">⚔️ BRACTWA I GANGI ⚔️</h2>
				<p className="gang-subtitle">
					Dołącz do gangu i wspólnie rządźcie światem!
				</p>
			</div>

			{/* ----- PRZYCISKI: MÓJ GANG + STWÓRZ GANG ----- */}
			<div className="gang-my-gang-wrapper">
				<button
					className="gang-my-gang-btn"
					onClick={() => {
						if (myGangName) {
							navigate(`/my-gang?name=${encodeURIComponent(myGangName)}`);
						} else {
							navigate("/my-gang");
						}
					}}
				>
					{myGangName ? `🏠 MÓJ GANG: ${myGangName}` : "🏠 MÓJ GANG"}
				</button>
				{!myGangName && (
					<>
						<span className="gang-no-gang-hint">
							Nie należysz jeszcze do żadnego gangu
						</span>
						<button className="gang-my-gang-btn" onClick={openCreateModal}>
							✨ STWÓRZ WŁASNY GANG
						</button>
					</>
				)}
			</div>

			{/* ----- TABELA GANGÓW ----- */}
			{gangs.length === 0 && !loading ? (
				<div className="gang-empty">
					<p>Brak gangów do wyświetlenia.</p>
				</div>
			) : (
				<>
					<div className="gang-table">
						<div className="gang-table-header">
							<div className="gang-rank-col">#</div>
							<div className="gang-emblem-col">EMBLEMAT</div>
							<div className="gang-name-col">NAZWA</div>
							<div className="gang-desc-col">OPIS</div>
							<div className="gang-leader-col">LIDER</div>
							<div className="gang-count-col">CZŁONKOWIE</div>
							<div className="gang-action-col">AKCJA</div>
						</div>

						<div className="gang-table-body">
							{gangs.map((gang, idx) => {
								const globalRank = getGlobalRank(idx) + 1;
								const isMyGang = myGangName === gang.gangName;

								return (
									<div
										key={gang.gangName}
										className={`gang-row ${isMyGang ? "gang-row--my" : ""}`}
									>
										<div className="gang-rank-col">
											<span className="gang-rank-number">{globalRank}</span>
										</div>
										<div className="gang-emblem-col">
											{gang.gangEmblem ? (
												<img
													src={`/gang_emblems/${gang.gangEmblem}.png`}
													alt={`Emblemat ${gang.gangEmblem}`}
													className="gang-emblem-img"
												/>
											) : (
												<div className="gang-emblem-placeholder">🛡️</div>
											)}
										</div>
										<div className="gang-name-col">
											{gang.gangName}
											{isMyGang && (
												<span className="gang-my-badge">TWÓJ GANG</span>
											)}
										</div>
										<div className="gang-desc-col">{gang.gangDesc}</div>
										<div className="gang-leader-col">{gang.gangLeader}</div>
										<div className="gang-count-col">
											<span className="gang-count-badge">
												{gang.membersCount}
											</span>
										</div>
										<div className="gang-action-col">
											{isMyGang ? (
												<button
													className="gang-action-btn gang-action-btn--my"
													onClick={() =>
														navigate(
															`/my-gang?name=${encodeURIComponent(gang.gangName)}`,
														)
													}
												>
													ZARZĄDZAJ
												</button>
											) : myGangName ? (
												<button
													className="gang-action-btn gang-action-btn--attack"
													onClick={() => handleAttackGang(gang.gangName)}
													disabled={joiningGang === gang.gangName}
												>
													{joiningGang === gang.gangName ? "..." : "ZAATAKUJ"}
												</button>
											) : (
												<button
													className="gang-action-btn gang-action-btn--join"
													onClick={() => handleJoinRequest(gang.gangName)}
													disabled={joiningGang === gang.gangName}
												>
													{joiningGang === gang.gangName
														? "Wysyłanie..."
														: "DOŁĄCZ"}
												</button>
											)}
										</div>
									</div>
								);
							})}
						</div>
					</div>

					{/* ----- PAGINACJA ----- */}
					<div className="gang-pagination">
						<div className="gang-pagination-info">
							Pokazuje {gangs.length} z {totalElements} gangów &nbsp; (strona{" "}
							{page + 1} z {totalPages})
						</div>
						<div className="gang-pagination-controls">
							<button
								className="gang-pagination-btn"
								onClick={goToPreviousPage}
								disabled={page === 0}
							>
								◀ Poprzednia
							</button>
							<button
								className="gang-pagination-btn"
								onClick={goToNextPage}
								disabled={page + 1 >= totalPages}
							>
								Następna ▶
							</button>
						</div>
					</div>
				</>
			)}

			{/* ===== MODAL: TWORZENIE GANGU ===== */}
			{showCreateModal && (
				<div
					className="gang-modal-overlay"
					onClick={() => setShowCreateModal(false)}
				>
					<div className="gang-modal" onClick={(e) => e.stopPropagation()}>
						{/* Nagłówek modala */}
						<div className="gang-modal-header">
							<h3>✨ Stwórz własny gang</h3>
							<button
								className="gang-modal-close"
								onClick={() => setShowCreateModal(false)}
							>
								✕
							</button>
						</div>

						{/* Formularz */}
						<form onSubmit={handleCreateGang}>
							<div className="gang-modal-body">
								{/* Nazwa gangu */}
								<div className="gang-modal-field">
									<label htmlFor="gang-name-input">Nazwa gangu</label>
									<input
										id="gang-name-input"
										type="text"
										className="gang-modal-input"
										placeholder="Wpisz nazwę gangu..."
										value={newGangName}
										onChange={(e) => setNewGangName(e.target.value)}
										maxLength={30}
										autoFocus
									/>
									<span className="gang-modal-char-count">
										{newGangName.length}/30
									</span>
								</div>

								{/* Opis gangu */}
								<div className="gang-modal-field">
									<label htmlFor="gang-desc-input">Opis gangu</label>
									<textarea
										id="gang-desc-input"
										className="gang-modal-textarea"
										placeholder="Opisz swój gang..."
										value={newGangDesc}
										onChange={(e) => setNewGangDesc(e.target.value)}
										maxLength={200}
										rows={3}
									/>
									<span className="gang-modal-char-count">
										{newGangDesc.length}/200
									</span>
								</div>

								{/* Wybór emblematu */}
								<div className="gang-modal-field">
									<label>Wybierz emblemat</label>
									<div className="gang-modal-emblems">
										{EMBLEM_LIST.map((emblem) => (
											<div
												key={emblem}
												className={`gang-modal-emblem-option ${
													selectedEmblem === emblem
														? "gang-modal-emblem-option--selected"
														: ""
												}`}
												onClick={() => setSelectedEmblem(emblem)}
											>
												<img
													src={`/gang_emblems/${emblem}.png`}
													alt={emblem}
													className="gang-modal-emblem-img"
												/>
												{selectedEmblem === emblem && (
													<span className="gang-modal-emblem-check">✓</span>
												)}
											</div>
										))}
									</div>
								</div>
							</div>

							{/* Stopka modala */}
							<div className="gang-modal-footer">
								<button
									type="button"
									className="gang-modal-btn gang-modal-btn--cancel"
									onClick={() => setShowCreateModal(false)}
									disabled={creating}
								>
									Anuluj
								</button>
								<button
									type="submit"
									className="gang-modal-btn gang-modal-btn--confirm"
									disabled={creating}
								>
									{creating ? "Tworzenie..." : "✨ Utwórz gang"}
								</button>
							</div>
						</form>
					</div>
				</div>
			)}
		</div>
	);
};

export default GangList;
