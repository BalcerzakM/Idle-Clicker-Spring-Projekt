import { useState, useEffect, useCallback, useRef } from "react";
import "../css/SecurityView.css";
import { useAlert } from "../context/AlertContext";
import { useCharacter } from "../context/CharacterContext";

interface BouncerDutyDto {
	dutyStartTime: string;
	dutyEndTime: string;
	reward: number;
}

interface ActiveQuestDto {
	questTitle: string;
	questStartTime: string;
	questEndTime: string;
	imagePath: string;
}

function Security() {
	const { refreshCharacter, character } = useCharacter();
	const { showError } = useAlert();

	const [hours, setHours] = useState<number>(4);
	const [loading, setLoading] = useState<boolean>(true);
	const [activeDuty, setActiveDuty] = useState<BouncerDutyDto | null>(null);
	const [activeQuest, setActiveQuest] = useState<ActiveQuestDto | null>(null); // nowy stan
	const [timeLeft, setTimeLeft] = useState<string>("");
	const [progress, setProgress] = useState<number>(100);
	const [totalDuration, setTotalDuration] = useState<number>(0);
	const [isDutyFinished, setIsDutyFinished] = useState<boolean>(false);
	const [earlyFinish, setEarlyFinish] = useState<boolean>(false);
	const [lastReward, setLastReward] = useState<number | null>(null);

	const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);
	const completingRef = useRef(false);

	const estimateReward = (hours: number): number => {
		const auraLvl = character?.auraLevel ?? 1;
		return Math.floor(10 * auraLvl * 1.5 * hours);
	};

	// Sprawdzenie aktywnego questa (podobnie jak w Barman)
	const checkActiveQuest = useCallback(async () => {
		try {
			const res = await fetch("http://localhost:8080/api/quest/active");
			if (res.status === 404 || res.status === 409) {
				setActiveQuest(null);
				return;
			}
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Błąd sprawdzania aktywnego zlecenia");
				setActiveQuest(null);
				return;
			}
			const data: ActiveQuestDto = await res.json();
			setActiveQuest(data);
		} catch (err) {
			console.error(err);
			// Nie pokazuj błędu, aby nie denerwować użytkownika przy każdym odświeżeniu
			setActiveQuest(null);
		}
	}, [showError]);

	const checkActiveDuty = useCallback(async () => {
		try {
			setLoading(true);
			const res = await fetch("http://localhost:8080/api/security");
			if (res.status === 404) {
				setActiveDuty(null);
				return;
			}
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Błąd pobierania zmiany");
				return;
			}
			const data: BouncerDutyDto = await res.json();
			setActiveDuty(data);
			const startMs = new Date(data.dutyStartTime).getTime();
			const endMs = new Date(data.dutyEndTime).getTime();
			setTotalDuration(endMs - startMs);
		} catch {
			showError("Brak połączenia z serwerem");
		} finally {
			setLoading(false);
		}
	}, [showError]);

	useEffect(() => {
		checkActiveQuest(); // sprawdź quest przy starcie
		checkActiveDuty();
	}, [checkActiveQuest, checkActiveDuty]);

	const handleStartDuty = async () => {
		// Blokada: jeśli aktywny quest, nie można rozpocząć ochrony
		if (activeQuest) {
			showError(
				"Nie możesz rozpocząć ochrony, gdy masz aktywne zlecenie u barmana.",
			);
			return;
		}

		try {
			setLoading(true);
			const res = await fetch("http://localhost:8080/api/security", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(hours),
			});
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Nie udało się rozpocząć zmiany");
				return;
			}
			const data: BouncerDutyDto = await res.json();
			setActiveDuty(data);
			const startMs = new Date(data.dutyStartTime).getTime();
			const endMs = new Date(data.dutyEndTime).getTime();
			setTotalDuration(endMs - startMs);
			setEarlyFinish(false);
		} catch {
			showError("Brak połączenia z serwerem");
		} finally {
			setLoading(false);
		}
	};

	// Odliczanie (bez zmian)
	useEffect(() => {
		if (!activeDuty || !activeDuty.dutyEndTime) return;

		const updateTimer = () => {
			const now = Date.now();
			const end = new Date(activeDuty.dutyEndTime).getTime();
			const diff = end - now;

			if (diff <= 0) {
				setTimeLeft("00:00:00");
				setProgress(0);
				setIsDutyFinished(true);
				if (timerRef.current) clearInterval(timerRef.current);
				return;
			}

			const hrs = Math.floor(diff / (1000 * 60 * 60));
			const mins = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
			const secs = Math.floor((diff % (1000 * 60)) / 1000);
			setTimeLeft(
				`${hrs.toString().padStart(2, "0")}:${mins
					.toString()
					.padStart(2, "0")}:${secs.toString().padStart(2, "0")}`,
			);

			if (totalDuration > 0) {
				const percentLeft = (diff / totalDuration) * 100;
				setProgress(Math.max(0, percentLeft));
			}
		};

		updateTimer();
		timerRef.current = setInterval(updateTimer, 1000);
		return () => {
			if (timerRef.current) clearInterval(timerRef.current);
		};
	}, [activeDuty, totalDuration]);

	useEffect(() => {
		return () => {
			if (timerRef.current) clearInterval(timerRef.current);
		};
	}, []);

	const handleFinishEarly = async () => {
		try {
			const res = await fetch("http://localhost:8080/api/security/clear", {
				method: "PATCH",
			});
			if (!res.ok) {
				showError("Nie udało się zakończyć zmiany");
				return;
			}
			setIsDutyFinished(true);
			setProgress(0);
			setTimeLeft("00:00:00");
			setEarlyFinish(true);
			if (timerRef.current) {
				clearInterval(timerRef.current);
			}
		} catch {
			showError("Brak połączenia z serwerem");
		}
	};

	const handleBackToSelection = async () => {
		if (completingRef.current) return;
		completingRef.current = true;

		if (earlyFinish) {
			await refreshCharacter();
			setActiveDuty(null);
			setIsDutyFinished(false);
			setProgress(100);
			setTimeLeft("");
			setTotalDuration(0);
			setEarlyFinish(false);
			setLastReward(null);
			completingRef.current = false;
			return;
		}

		try {
			const res = await fetch("http://localhost:8080/api/security/complete", {
				method: "POST",
			});
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Błąd odbierania nagrody");
				completingRef.current = false;
				return;
			}
			const data = await res.json();
			setLastReward(data.reward);
		} catch {
			showError("Brak połączenia z serwerem");
		} finally {
			await refreshCharacter();
			setActiveDuty(null);
			setIsDutyFinished(false);
			setProgress(100);
			setTimeLeft("");
			setTotalDuration(0);
			setEarlyFinish(false);
			completingRef.current = false;
		}
	};

	if (loading) return <div className="quest-loading">Ładowanie...</div>;

	// Widok aktywnej zmiany (bez zmian)
	if (activeDuty) {
		return (
			<div className="security-container">
				<div className="security-active-background">
					<div className="security-active-right-panel">
						<h2 className="security-active-title">Ochrona klubu</h2>

						{isDutyFinished ? (
							<div className="security-finished-box">
								<p className="security-timer-hint">
									Zmiana zakończona! Czas odpocząć.
								</p>
								{!earlyFinish && (
									<p className="security-reward">
										Nagroda: 💰 {activeDuty.reward} monet
									</p>
								)}
								<button className="quest-btn" onClick={handleBackToSelection}>
									WRÓĆ
								</button>
							</div>
						) : (
							<>
								<p className="security-timer-label">Pozostało</p>
								<p className="security-timer">{timeLeft}</p>
								<p className="security-timer-hint">
									Twoja zmiana dobiegnie końca automatycznie.
								</p>
								<p className="security-reward">
									Nagroda: 💰 {activeDuty.reward} monet
								</p>
								<button
									className="quest-btn"
									style={{ marginTop: "20px", fontSize: "14px" }}
									onClick={handleFinishEarly}
								>
									ZAKOŃCZ WCZEŚNIEJ
								</button>
								<p className="security-timer-hint">
									Jeśli zakończysz wcześniej, nie otrzymasz nagrody.
								</p>
							</>
						)}

						<div className="security-progress-container">
							<div
								className="security-progress-fill"
								style={{ width: `${progress}%` }}
							/>
						</div>
					</div>
				</div>
			</div>
		);
	}

	// Widok wyboru godzin – z dodatkowym komunikatem blokady
	return (
		<div className="security-container">
			<div className="security-left-image" />
			<div className="security-right-panel">
				<h2 className="security-panel-title">Stanie na bramkach</h2>

				{lastReward !== null && (
					<div className="security-last-reward">
						Ostatnia zmiana: otrzymałeś 💰 {lastReward} monet!
					</div>
				)}

				{/* Informacja o aktywnym queście, jeśli istnieje */}
				{activeQuest && (
					<div
						className="security-last-reward"
						style={{ backgroundColor: "#9e1d02", color: "white" }}
					>
						⚠️ Masz aktywne zlecenie u barmana! Zakończ je, zanim rozpoczniesz
						ochronę.
					</div>
				)}

				<p className="security-description">
					Wybierz ile godzin chcesz przepracować jako bramkarz. Im dłużej, tym
					więcej zarobisz!
				</p>
				<div className="security-slider-container">
					<label htmlFor="hours-slider" className="security-slider-label">
						Liczba godzin: <span>{hours}</span>
					</label>
					<input
						id="hours-slider"
						type="range"
						min="1"
						max="10"
						value={hours}
						onChange={(e) => setHours(Number(e.target.value))}
						className="security-slider"
						disabled={!!activeQuest}
					/>
					<div className="security-slider-limits">
						<span>1h</span>
						<span>10h</span>
					</div>
				</div>
				<div className="security-estimated-reward">
					Szacowana nagroda: <span>💰 {estimateReward(hours)} monet</span>
				</div>
				<button
					className="quest-btn security-start-btn"
					onClick={handleStartDuty}
					disabled={loading || !!activeQuest}
				>
					{activeQuest ? "Zlecenie u barmana aktywne" : "Zacznij zmianę"}
				</button>
			</div>
		</div>
	);
}

export default Security;
