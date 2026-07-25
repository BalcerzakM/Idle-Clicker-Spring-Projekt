import { useState, useEffect, useCallback } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import "../css/GangView.css";
import { useAlert } from "../context/AlertContext.tsx";
import { useCharacter } from "../context/CharacterContext";
import GangArena from "../components/GangArena";
import StandardCurrencyImg from "../assets/other/currency_standard.png";

// ----- INTERFACES -----
interface CharacterDto {
	name: string;
	characterClass?: string;
	auraLvl?: number;
}

interface CharacterInBattleDto {
	characterName: string;
	imagePath: string;
	hp: number;
}

interface FullGangInfoDto {
	membersCount: number;
	members: CharacterDto[];
	gangName: string;
	gangDesc: string;
	gangLeader: string;
	gangEmblem: string;
	requests: string[];
	moneyBank: number;
	cristalBank: number;
	gangToAttackName: string | null;
	votes: number;
}

export interface GangCombatDto {
	combatLog: number[];
	teamAWon: boolean;
	teamAImageFolder: string;
	teamBImageFolder: string;
	teamACharacters: CharacterInBattleDto[]; // 👈 zmienione
	teamBCharacters: CharacterInBattleDto[]; // 👈 zmienione
	moneyReward: number;
	auraReward: number;
	itemReward: any | null;
}

const MyGang = () => {
	const { showError, showInfo } = useAlert();
	const { character, refreshCharacter } = useCharacter();
	const navigate = useNavigate();
	const [searchParams] = useSearchParams();

	// ----- STANY -----
	const [gangInfo, setGangInfo] = useState<FullGangInfoDto | null>(null);
	const [loading, setLoading] = useState<boolean>(true);
	const [currentCharName, setCurrentCharName] = useState<string>("");
	const [depositMoneyAmount, setDepositMoneyAmount] = useState<number>(0);
	// const [depositCristalAmount, setDepositCristalAmount] = useState<number>(0); // do wpłacania kryształów w przyszłości
	const [depositing, setDepositing] = useState<"money" | "cristals" | null>(
		null,
	);
	const [processingRequest, setProcessingRequest] = useState<string | null>(
		null,
	);
	const [removingMember, setRemovingMember] = useState<string | null>(null);
	const [newMemberName, setNewMemberName] = useState("");
	const [voteProcessing, setVoteProcessing] = useState(false);

	const [gangCombat, setGangCombat] = useState<GangCombatDto | null>(null);
	const [watchingCombat, setWatchingCombat] = useState(false);

	const gangName = searchParams.get("name") || gangInfo?.gangName || "";

	// ----- POBIERANIE NAZWY POSTACI -----
	useEffect(() => {
		const fetchCharName = async () => {
			try {
				const res = await fetch("http://localhost:8080/api/character", {
					credentials: "include",
				});
				if (res.ok) {
					const data = await res.json();
					setCurrentCharName(data.name);
				}
			} catch {
				// cicho
			}
		};
		fetchCharName();
	}, [character]);

	// ----- POBIERANIE PEŁNYCH DANYCH GANGU -----
	const fetchGangInfo = useCallback(async () => {
		if (!gangName) {
			setLoading(false);
			return;
		}

		setLoading(true);
		try {
			const res = await fetch(
				`http://localhost:8080/api/gang/${encodeURIComponent(gangName)}/info`,
				{ credentials: "include" },
			);

			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Nie udało się pobrać danych gangu");
			}

			const data: FullGangInfoDto = await res.json();
			setGangInfo(data);
		} catch (err: any) {
			showError(err.message);
		} finally {
			setLoading(false);
		}
	}, [gangName, showError]);

	useEffect(() => {
		fetchGangInfo();
	}, [fetchGangInfo]);

	const isLeader = currentCharName === gangInfo?.gangLeader;

	// ----- WPŁATA PIENIĘDZY -----
	const handleDepositMoney = async () => {
		if (depositMoneyAmount <= 0) {
			showError("Podaj kwotę większą od 0");
			return;
		}
		try {
			setDepositing("money");
			const res = await fetch(
				`http://localhost:8080/api/gang/${encodeURIComponent(gangName)}/depositMoney?amount=${depositMoneyAmount}`,
				{ method: "PUT", credentials: "include" },
			);
			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Błąd wpłaty");
			}
			setDepositMoneyAmount(0);
			refreshCharacter();
			fetchGangInfo();
			showInfo("Wpłacono pieniądze!");
		} catch (err: any) {
			showError(err.message);
		} finally {
			setDepositing(null);
		}
	};

	//pobranie powtórki bitwy gangów
	const fetchGangCombat = useCallback(async () => {
		if (!gangName) return;
		try {
			const res = await fetch("http://localhost:8080/api/gang/watch", {
				credentials: "include",
			});
			if (res.ok) {
				const data: GangCombatDto = await res.json();
				setGangCombat(data);
			} else {
				// Jeśli nie ma żadnej walki (404), ustawiamy null
				setGangCombat(null);
			}
		} catch {
			setGangCombat(null);
		}
	}, [gangName]);

	useEffect(() => {
		fetchGangCombat();
	}, [fetchGangCombat]);

	// ----- WPŁATA KRYSZTAŁÓW -----
	// const handleDepositCristals = async () => {
	// 	if (depositCristalAmount <= 0) {
	// 		showError("Podaj ilość większą od 0");
	// 		return;
	// 	}
	// 	try {
	// 		setDepositing("cristals");
	// 		const res = await fetch(
	// 			`http://localhost:8080/api/gang/${encodeURIComponent(gangName)}/depositCristals?amount=${depositCristalAmount}`,
	// 			{ method: "PUT", credentials: "include" },
	// 		);
	// 		if (!res.ok) {
	// 			const error = await res.json();
	// 			throw new Error(error.message || "Błąd wpłaty");
	// 		}
	// 		setDepositCristalAmount(0);
	// 		refreshCharacter();
	// 		fetchGangInfo();
	// 		showInfo("Wpłacono kryształy!");
	// 	} catch (err: any) {
	// 		showError(err.message);
	// 	} finally {
	// 		setDepositing(null);
	// 	}
	// };

	// ----- AKCEPTACJA PROŚBY -----
	const handleAcceptRequest = async (charName: string) => {
		try {
			setProcessingRequest(charName);
			const res = await fetch(
				`http://localhost:8080/api/gang/${encodeURIComponent(gangName)}/acceptRequest?characterName=${encodeURIComponent(charName)}`,
				{ method: "POST", credentials: "include" },
			);
			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Błąd akceptacji");
			}
			fetchGangInfo();
		} catch (err: any) {
			showError(err.message);
		} finally {
			setProcessingRequest(null);
		}
	};

	// ----- ODRZUCENIE PROŚBY -----
	const handleRejectRequest = async (charName: string) => {
		try {
			setProcessingRequest(charName);
			const res = await fetch(
				`http://localhost:8080/api/gang/${encodeURIComponent(gangName)}/rejectRequest?characterName=${encodeURIComponent(charName)}`,
				{ method: "POST", credentials: "include" },
			);
			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Błąd odrzucenia");
			}
			fetchGangInfo();
		} catch (err: any) {
			showError(err.message);
		} finally {
			setProcessingRequest(null);
		}
	};

	// ----- USUWANIE CZŁONKA -----
	const handleRemoveMember = async (charName: string) => {
		if (!window.confirm(`Czy na pewno usunąć ${charName} z gangu?`)) return;
		try {
			setRemovingMember(charName);
			const res = await fetch(
				`http://localhost:8080/api/gang/removeMember?characterName=${encodeURIComponent(charName)}`,
				{ method: "DELETE", credentials: "include" },
			);
			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Błąd usuwania");
			}
			fetchGangInfo();
		} catch (err: any) {
			showError(err.message);
		} finally {
			setRemovingMember(null);
		}
	};

	// ----- OPUSZCZENIE GANGU -----
	const handleQuitGang = async () => {
		if (!window.confirm("Czy na pewno chcesz opuścić gang?")) return;
		try {
			const res = await fetch("http://localhost:8080/api/gang/quitGang", {
				method: "DELETE",
				credentials: "include",
			});
			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Błąd opuszczania");
			}
			refreshCharacter();
			navigate("/gang-list");
		} catch (err: any) {
			showError(err.message);
		}
	};

	// ----- DODAWANIE CZŁONKA (LIDER) -----
	const handleAddMember = async () => {
		if (!newMemberName.trim()) return;
		try {
			const res = await fetch(
				`http://localhost:8080/api/gang/${encodeURIComponent(gangName)}/addMember?characterName=${encodeURIComponent(newMemberName.trim())}`,
				{ method: "POST", credentials: "include" },
			);
			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Błąd dodawania");
			}
			setNewMemberName("");
			fetchGangInfo();
		} catch (err: any) {
			showError(err.message);
		}
	};

	// ----- ODDANIE GŁOSU -----
	const handleVote = async () => {
		try {
			setVoteProcessing(true);
			const res = await fetch("http://localhost:8080/api/gang/vote", {
				method: "PATCH",
				credentials: "include",
			});
			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Błąd oddania głosu");
			}
			fetchGangInfo();
		} catch (err: any) {
			showError(err.message);
		} finally {
			setVoteProcessing(false);
		}
	};

	// ----- ROZPOCZĘCIE BITWY -----
	const handleStartBattle = async () => {
		try {
			const res = await fetch("http://localhost:8080/api/gang/startBattle", {
				method: "POST",
				credentials: "include",
			});
			if (!res.ok) {
				const error = await res.json();
				throw new Error(error.message || "Nie udało się rozpocząć walki");
			}
			showInfo("Walka rozpoczęta!");
			// Ewentualnie: nawiguj do widoku bitwy
			fetchGangInfo();
		} catch (err: any) {
			showError(err.message);
		}
	};

	// Obliczanie wymaganej liczby głosów (połowa zaokrąglona w górę)
	const requiredVotes = gangInfo ? Math.ceil(gangInfo.membersCount / 2) : 0;
	const voteReached = gangInfo ? gangInfo.votes >= requiredVotes : false;

	// ----- RENDER -----
	if (loading) {
		return (
			<div className="gang-container">
				<div className="gang-loading-overlay">Ładowanie danych gangu...</div>
			</div>
		);
	}

	if (!gangName || !gangInfo) {
		return (
			<div className="gang-container">
				<div className="gang-header-section">
					<h2 className="gang-title">⚔️ MÓJ GANG ⚔️</h2>
				</div>
				<div className="gang-empty">
					<p>Nie należysz do żadnego gangu.</p>
					<button
						className="gang-my-gang-btn"
						onClick={() => navigate("/gang-list")}
					>
						PRZEGLĄDAJ GANGI
					</button>
				</div>
			</div>
		);
	}

	if (watchingCombat && gangCombat) {
		return (
			<GangArena
				combatData={gangCombat}
				onClose={() => {
					setWatchingCombat(false);
					refreshCharacter();
					fetchGangInfo();
				}}
			/>
		);
	}

	return (
		<div className="gang-container gang-container--my">
			{/* ----- NAGŁÓWEK GANGU ----- */}
			<div className="gang-my-header">
				<div className="gang-my-emblem">
					{gangInfo.gangEmblem ? (
						<img
							src={`/gang_emblems/${gangInfo.gangEmblem}.png`}
							alt={`Emblemat + ${gangInfo.gangEmblem}`}
							className="gang-my-emblem-img"
						/>
					) : (
						<div className="gang-my-emblem-placeholder">🛡️</div>
					)}
				</div>
				<div className="gang-my-info">
					<h2 className="gang-my-name">{gangInfo.gangName}</h2>
					<p className="gang-my-desc">{gangInfo.gangDesc}</p>
					<p className="gang-my-leader">
						👑 Lider: <strong>{gangInfo.gangLeader}</strong>
					</p>
					<p className="gang-my-count">
						👥 Członkowie: <strong>{gangInfo.membersCount}</strong>
					</p>
				</div>
				<button className="gang-quit-btn" onClick={handleQuitGang}>
					🚪 OPUŚĆ GANG
				</button>
			</div>

			{/* ----- GŁÓWNA SEKCJA ----- */}
			<div className="gang-my-content">
				{/* ----- LEWA KOLUMNA: CZŁONKOWIE ----- */}
				<div className="gang-my-section gang-my-members">
					<h3 className="gang-section-title">👥 CZŁONKOWIE GANGU</h3>
					<div className="gang-members-list">
						{gangInfo.members.map((member) => (
							<div
								key={member.name}
								className={`gang-member-row ${member.name === gangInfo.gangLeader ? "gang-member-row--leader" : ""}`}
							>
								<span className="gang-member-name">
									{member.name === gangInfo.gangLeader && "👑 "}
									{member.name}
								</span>
								{member.characterClass && (
									<span className="gang-member-class">
										{member.characterClass}
									</span>
								)}
								{member.auraLvl !== undefined && (
									<span className="gang-member-aura">✨ {member.auraLvl}</span>
								)}
								{isLeader && member.name !== gangInfo.gangLeader && (
									<button
										className="gang-remove-member-btn"
										onClick={() => handleRemoveMember(member.name)}
										disabled={removingMember === member.name}
									>
										{removingMember === member.name ? "..." : "❌"}
									</button>
								)}
							</div>
						))}
					</div>

					{/* Szybkie dodawanie członka (tylko lider) */}
					{isLeader && (
						<div className="gang-add-member">
							<input
								type="text"
								placeholder="Nazwa postaci..."
								value={newMemberName}
								onChange={(e) => setNewMemberName(e.target.value)}
								className="gang-input"
							/>
							<button
								className="gang-action-btn gang-action-btn--join"
								onClick={handleAddMember}
							>
								DODAJ
							</button>
						</div>
					)}
				</div>

				{/* ----- PRAWA KOLUMNA: SKARBCE + PROŚBY + GŁOSOWANIE ----- */}
				<div className="gang-my-right">
					{/* ----- SKARBIEC ----- */}
					<div className="gang-my-section gang-my-treasury">
						<h3 className="gang-section-title">💰 SKARBIEC</h3>
						<div className="gang-treasury-row">
							<div className="gang-treasury-item">
								<span className="gang-treasury-label">💵 PIENIĄDZE</span>
								<span className="gang-treasury-value">
									<span className="gang-cost1">
										{gangInfo.moneyBank.toLocaleString()}{" "}
										<img
											src={StandardCurrencyImg}
											alt="Standard currency"
											className="currencyImg-icon"
										/>
									</span>
								</span>
								<div className="gang-deposit">
									<input
										type="number"
										min="1"
										value={depositMoneyAmount}
										onChange={(e) =>
											setDepositMoneyAmount(Number(e.target.value))
										}
										className="gang-input gang-input--small"
										placeholder="Kwota"
									/>
									<button
										className="gang-action-btn gang-action-btn--deposit"
										onClick={handleDepositMoney}
										disabled={depositing === "money"}
									>
										{depositing === "money" ? "..." : "WPŁAĆ"}
									</button>
								</div>
							</div>
							{/* <div className="gang-treasury-item">
								<span className="gang-treasury-label">💎 KRYSZTAŁY</span>
								<span className="gang-treasury-value">
									{gangInfo.cristalBank.toLocaleString()} szt.
								</span>
								<div className="gang-deposit">
									<input
										type="number"
										min="1"
										value={depositCristalAmount}
										onChange={(e) =>
											setDepositCristalAmount(Number(e.target.value))
										}
										className="gang-input gang-input--small"
										placeholder="Ilość"
									/>
									<button
										className="gang-action-btn gang-action-btn--deposit"
										onClick={handleDepositCristals}
										disabled={depositing === "cristals"}
									>
										{depositing === "cristals" ? "..." : "WPŁAĆ"}
									</button>
								</div>
							</div> */}
						</div>
					</div>

					{/* ----- PROŚBY O DOŁĄCZENIE ----- */}
					<div className="gang-my-section gang-my-requests">
						<h3 className="gang-section-title">📩 PROŚBY O DOŁĄCZENIE</h3>
						{gangInfo.requests.length === 0 ? (
							<p className="gang-no-requests">Brak oczekujących próśb</p>
						) : (
							<div className="gang-requests-list">
								{gangInfo.requests.map((reqName) => (
									<div key={reqName} className="gang-request-row">
										<span className="gang-request-name">{reqName}</span>
										{isLeader && (
											<div className="gang-request-actions">
												<button
													className="gang-action-btn gang-action-btn--accept"
													onClick={() => handleAcceptRequest(reqName)}
													disabled={processingRequest === reqName}
												>
													{processingRequest === reqName ? "..." : "✅"}
												</button>
												<button
													className="gang-action-btn gang-action-btn--reject"
													onClick={() => handleRejectRequest(reqName)}
													disabled={processingRequest === reqName}
												>
													{processingRequest === reqName ? "..." : "❌"}
												</button>
											</div>
										)}
									</div>
								))}
							</div>
						)}
					</div>

					{/* ----- GŁOSOWANIE O WALKĘ ----- */}
					<div className="gang-my-section gang-my-voting">
						<h3 className="gang-section-title">⚔️ GŁOSOWANIE O WALKĘ</h3>

						{/* Jeśli trwa aktywne głosowanie */}
						{gangInfo.gangToAttackName ? (
							<div className="gang-vote-active">
								<p className="gang-vote-target">
									🎯 Cel: <strong>{gangInfo.gangToAttackName}</strong>
								</p>
								<div className="gang-vote-counter">
									<span className="gang-vote-count">
										🗳️ Głosy: {gangInfo.votes} / {requiredVotes}
									</span>
									<div className="gang-vote-bar">
										<div
											className="gang-vote-bar-fill"
											style={{
												width: `${Math.min(
													(gangInfo.votes / requiredVotes) * 100,
													100,
												)}%`,
											}}
										></div>
									</div>
								</div>

								{voteReached ? (
									<div className="gang-vote-actions">
										<span className="gang-vote-success">
											✅ Wymagana liczba głosów osiągnięta!
										</span>
										{isLeader && (
											<button
												className="gang-action-btn gang-action-btn--attack"
												onClick={handleStartBattle}
											>
												⚔️ ZAATAKUJ (koszt:{" "}
												<span className="gang-cost">
													300
													<img
														src={StandardCurrencyImg}
														alt="Standard currency"
														className="currencyImg-icon"
													/>{" "}
													)
												</span>
											</button>
										)}
									</div>
								) : (
									<div className="gang-vote-actions">
										<button
											className="gang-action-btn gang-action-btn--join"
											onClick={handleVote}
											disabled={voteProcessing}
										>
											{voteProcessing ? "..." : "🗳️ ZAGŁOSUJ"}
										</button>
									</div>
								)}
							</div>
						) : (
							<div className="gang-no-vote">
								<p className="gang-no-requests">Brak aktywnego głosowania</p>
							</div>
						)}
					</div>
					<div className="gang-my-section gang-my-last-war">
						<h3 className="gang-section-title">⚔️ OSTATNIA WOJNA GANGU</h3>
						{gangCombat ? (
							<div className="gang-last-war-info">
								<p>Dostępna jest powtórka ostatniej walki!</p>
								<button
									className="gang-action-btn gang-action-btn--watch"
									onClick={() => setWatchingCombat(true)}
								>
									🎥 OBEJRZYJ WALKĘ
								</button>
							</div>
						) : (
							<p className="gang-no-requests">Brak nagranych walk</p>
						)}
					</div>
				</div>
			</div>

			{/* ----- PRZYCISK POWROTU ----- */}
			<div className="gang-my-back">
				<button
					className="gang-pagination-btn"
					onClick={() => navigate("/gang-list")}
				>
					◀ POWRÓT DO LISTY
				</button>
			</div>
		</div>
	);
};

export default MyGang;
