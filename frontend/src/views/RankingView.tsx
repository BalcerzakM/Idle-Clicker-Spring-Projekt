import { useState, useEffect, useCallback } from "react";
import "../css/RankingView.css";
import { useAlert } from "../context/AlertContext.tsx";

// Typy danych
interface CharacterDto {
	name: string;
	characterClass: string;
	auraLvl: number;
}

interface PageResponse<T> {
	content: T[];
	totalPages: number;
	totalElements: number;
	number: number; // bieżący numer strony (0-index)
	size: number;
}
export interface RankingPlayerDto {
	characterName: string;
	avatarPicture: string;
	eqItemsPicrures: Record<string, string>;

	auraLvl: number;

	totalRizz: number;
	totalStrength: number;
	totalAgility: number;
	totalEndurance: number;
	totalLuck: number;
}

const RankingView = () => {
	const { showError } = useAlert();

	// Stan rankingu
	const [ranking, setRanking] = useState<CharacterDto[]>([]);
	const [loading, setLoading] = useState<boolean>(false);

	// Stany paginacji
	const [page, setPage] = useState<number>(0);
	const [pageSize] = useState<number>(10);
	const [totalPages, setTotalPages] = useState<number>(0);
	const [totalElements, setTotalElements] = useState<number>(0);
	const [search, setSearch] = useState("");
	const [debouncedSearch, setDebouncedSearch] = useState("");
	const [searchedPlayer, setSearchedPlayer] = useState("");

	const [expandedPlayer, setExpandedPlayer] = useState<string | null>(null);

	const [playerDetails, setPlayerDetails] = useState<
		Record<string, RankingPlayerDto>
	>({});

	const loadPlayerDetails = async (playerName: string) => {
		if (playerDetails[playerName]) {
			return;
		}

		try {
			const response = await fetch(
				`http://localhost:8080/api/ranking/player/${playerName}`,
				{
					credentials: "include",
				},
			);

			if (!response.ok) {
				throw new Error("Nie udało się pobrać danych gracza");
			}

			const data: RankingPlayerDto = await response.json();

			setPlayerDetails((prev) => ({
				...prev,
				[playerName]: data,
			}));
		} catch (err: any) {
			showError(err.message);
		}
	};

	const handleRowClick = async (playerName: string) => {
		if (expandedPlayer === playerName) {
			setExpandedPlayer(null);
			return;
		}

		await loadPlayerDetails(playerName);
		setExpandedPlayer(playerName);
	};

	const EQUIPMENT_SLOTS = [
		"HEAD",
		"NECK",
		"UPPER_BODY",
		"LOWER_BODY",
		"FEET",
		"WRIST",
		"EMBLEM",
	];

	// Pobieranie danych z backendu
	const fetchRanking = useCallback(async () => {
		setLoading(true);
		try {
			const params = new URLSearchParams({
				page: page.toString(),
				size: pageSize.toString(),
				sort: "auraLvl,desc",
			});

			const response = await fetch(
				`http://localhost:8080/api/ranking?${params.toString()}`,
				{
					credentials: "include",
				},
			);

			if (!response.ok) {
				const errorData = await response.json();
				throw new Error(errorData.message || "Nie udało się pobrać rankingu");
			}

			const data: PageResponse<CharacterDto> = await response.json();
			setRanking(data.content);
			setTotalPages(data.totalPages);
			setTotalElements(data.totalElements);
		} catch (err: any) {
			console.error(err);
			showError(err.message || "Błąd połączenia z serwerem");
			setRanking([]);
			setTotalPages(0);
			setTotalElements(0);
		} finally {
			setLoading(false);
		}
	}, [page, pageSize, showError]);

	// Ponowne ładowanie przy zmianie strony lub rozmiaru strony
	useEffect(() => {
		fetchRanking();
	}, [fetchRanking]);

	useEffect(() => {
		if (!debouncedSearch.trim()) {
			return;
		}

		const findPlayer = async () => {
			try {
				console.log("page", page);
				console.log("pageSize", pageSize);
				const params = new URLSearchParams({
					name: debouncedSearch,
					pageSize: pageSize.toString(),
				});

				const response = await fetch(
					`http://localhost:8080/api/ranking/search?${params}`,
					{
						credentials: "include",
					},
				);

				if (!response.ok) {
					return;
				}

				const data = await response.json();

				setSearchedPlayer(debouncedSearch);
				setPage(data.page ?? 0);
			} catch (err) {
				console.error(err);
			}
		};

		findPlayer();
	}, [debouncedSearch, pageSize]);

	// Zmiana strony
	const goToPreviousPage = () => {
		if (page > 0) setPage(page - 1);
	};

	const goToNextPage = () => {
		if (page + 1 < totalPages) setPage(page + 1);
	};

	// Pomocnicza funkcja do wyświetlania medalu dla top3
	const getRankMedal = (rank: number) => {
		if (rank === 1) return "🥇";
		if (rank === 2) return "🥈";
		if (rank === 3) return "🥉";
		return null;
	};

	// Obliczanie globalnego indeksu w rankingu
	const getGlobalRank = (index: number) => page * pageSize + index + 1;

	useEffect(() => {
		const timeout = setTimeout(() => {
			setDebouncedSearch(search);
		}, 300);

		return () => clearTimeout(timeout);
	}, [search]);

	return (
		<div className="ranking-container">
			{loading && (
				<div className="ranking-loading-overlay">Aktualizacja rankingu...</div>
			)}
			<div className="ranking-header-section">
				<h2 className="ranking-title">🏆 TABLICA SŁAWY 🏆</h2>
				<p className="ranking-subtitle">
					Najwięksi wojownicy według poziomu Aury
				</p>
			</div>
			<div className="ranking-search">
				<span className="search-icon">🔍</span>
				<input
					type="text"
					placeholder="Szukaj postaci..."
					value={search}
					onChange={(e) => {
						setSearch(e.target.value);
					}}
				/>
			</div>

			{ranking.length === 0 ? (
				<div className="ranking-empty">
					<p>Brak postaci w rankingu.</p>
				</div>
			) : (
				<>
					<div className="ranking-table">
						<div className="ranking-table-header">
							<div className="rank-col">#</div>
							<div className="name-col">NAZWA</div>
							<div className="class-col">KLASA</div>
							<div className="aura-col">POZIOM AURY ✨</div>
						</div>

						<div className="ranking-table-body">
							{ranking.map((character, idx) => {
								const globalRank = getGlobalRank(idx);
								const medal = getRankMedal(globalRank);
								const isTop3 = globalRank <= 3;

								const isSearched =
									character.name.toLowerCase() === searchedPlayer.toLowerCase();

								const details = playerDetails[character.name];
								const expanded = expandedPlayer === character.name;

								return (
									<div key={`${character.name}-${globalRank}`}>
										<div
											className={`ranking-row
            ${isTop3 ? "ranking-row--top" : ""}
            ${isSearched ? "ranking-row--searched" : ""}`}
											onClick={() => handleRowClick(character.name)}
										>
											<div className="rank-col">
												{medal ? (
													<span className="rank-medal">{medal}</span>
												) : (
													<span className="rank-number">{globalRank}</span>
												)}
											</div>

											<div className="name-col">{character.name}</div>

											<div className="class-col">
												<span className="character-class-badge">
													{character.characterClass}
												</span>
											</div>

											<div className="aura-col">
												<span className="aura-value">{character.auraLvl}</span>
											</div>
										</div>

										{expanded && details && (
											<div className="ranking-player-details">
												<div className="ranking-player-stats">
													<span>✨ {details.totalRizz}</span>
													<span>💪 {details.totalStrength}</span>
													<span>🏃 {details.totalAgility}</span>
													<span>🛡️ {details.totalEndurance}</span>
													<span>🍀 {details.totalLuck}</span>
												</div>

												<div className="ranking-equipment">
													<div className="ranking-portrait">
														<img
															src={`/avatars/${details.avatarPicture}`}
															alt={details.characterName}
															className="ranking-portrait-img"
														/>
													</div>

													{EQUIPMENT_SLOTS.map((slot) => {
														const image = details.eqItemsPicrures?.[slot];

														return (
															<div
																key={slot}
																className="ranking-equip-slot"
																data-slot={slot}
															>
																{image ? (
																	<img
																		src={`/items/${image}`}
																		alt={slot}
																		className="ranking-item-icon"
																	/>
																) : (
																	<span className="slot-placeholder">
																		{slot}
																	</span>
																)}
															</div>
														);
													})}
												</div>
											</div>
										)}
									</div>
								);
							})}
						</div>
					</div>

					{/* Paginacja */}
					<div className="ranking-pagination">
						<div className="pagination-info">
							Pokazuje {ranking.length} z {totalElements} postaci &nbsp; (strona{" "}
							{page + 1} z {totalPages})
						</div>

						<div className="pagination-controls">
							<button
								className="pagination-btn"
								onClick={goToPreviousPage}
								disabled={page === 0}
							>
								◀ Poprzednia
							</button>

							<button
								className="pagination-btn"
								onClick={goToNextPage}
								disabled={page + 1 >= totalPages}
							>
								Następna ▶
							</button>
						</div>
					</div>
				</>
			)}
		</div>
	);
};

export default RankingView;
