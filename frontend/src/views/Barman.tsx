import { useState, useEffect, useCallback, useRef } from "react";
import "../css/QuestView.css";

// ----- typy -----
interface QuestDto {
	questId: number;
	questTitle: string;
	questDescription: string;
	questTier: "EASY" | "MEDIUM" | "HARD";
	questType: string;
	opponentName: string;
	questTime: number; // w sekundach?
	moneyReward: number;
	auraReward: number;
}

interface ActiveQuestDto {
	questTitle: string;
	questEndTime: string; // Instant jako string ISO
	imagePath: string;
}

function Barman() {
	const [quests, setQuests] = useState<QuestDto[]>([]);
	const [activeQuest, setActiveQuest] = useState<ActiveQuestDto | null>(null);
	const [loading, setLoading] = useState<boolean>(true);
	const [error, setError] = useState<string | null>(null);
	const [timeLeft, setTimeLeft] = useState<string>("");
	const [progress, setProgress] = useState<number>(100);
	const [totalDuration, setTotalDuration] = useState<number>(0);

	const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);

	const fetchQuests = useCallback(async () => {
		try {
			setLoading(true);
			const res = await fetch("http://localhost:8080/api/quest");
			if (!res.ok) throw new Error("Nie udało się pobrać questów");
			const data: QuestDto[] = await res.json();
			setQuests(data);
		} catch (err: any) {
			setError(err.message);
		} finally {
			setLoading(false);
		}
	}, []);

	const checkActiveQuest = useCallback(async () => {
		try {
			const res = await fetch("http://localhost:8080/api/quest/active");
			if (res.status === 404) {
				await fetchQuests();
				return;
			}
			if (!res.ok) throw new Error("Błąd podczas sprawdzania aktywnego questa");
			const data: ActiveQuestDto = await res.json();
			setActiveQuest(data);
		} catch (err: any) {
			setError(err.message);
		} finally {
			setLoading(false);
		}
	}, [fetchQuests]);

	useEffect(() => {
		checkActiveQuest();
	}, [checkActiveQuest]);

	const handleSelectQuest = async (questId: number) => {
		try {
			setError(null);
			const res = await fetch("http://localhost:8080/api/quest/active", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(questId),
			});
			if (!res.ok) throw new Error("Nie udało się wybrać questa");
			const data: ActiveQuestDto = await res.json();

			// zapamiętaj całkowity czas trwania na podstawie wybranego questa
			const selectedQuest = quests.find((q) => q.questId === questId);
			if (selectedQuest) {
				setTotalDuration(selectedQuest.questTime);
			}

			setActiveQuest(data);
		} catch (err: any) {
			setError(err.message || "Wystąpił błąd");
		}
	};

	// odliczanie i pasek postępu
	useEffect(() => {
		if (!activeQuest || totalDuration <= 0) return;

		const updateTimer = () => {
			const now = Date.now();
			const end = new Date(activeQuest.questEndTime).getTime();
			const diff = end - now;

			if (diff <= 0) {
				setTimeLeft("Ukończono!");
				setProgress(0);
				if (timerRef.current) clearInterval(timerRef.current);
				return;
			}

			const hours = Math.floor(diff / (1000 * 60 * 60));
			const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
			const seconds = Math.floor((diff % (1000 * 60)) / 1000);
			setTimeLeft(
				`${hours.toString().padStart(2, "0")}:${minutes
					.toString()
					.padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`,
			);

			// oblicz postęp (procent pozostałego czasu)
			const percentLeft = (diff / (totalDuration * 1000)) * 100;
			setProgress(Math.max(0, percentLeft));
		};

		updateTimer();
		timerRef.current = setInterval(updateTimer, 1000);

		return () => {
			if (timerRef.current) clearInterval(timerRef.current);
		};
	}, [activeQuest, totalDuration]);

	useEffect(() => {
		return () => {
			if (timerRef.current) clearInterval(timerRef.current);
		};
	}, []);

	if (loading) return <div className="quest-loading">Ładowanie...</div>;
	if (error) return <div className="quest-error">{error}</div>;

	// EKRAN AKTYWNEGO QUESTA (odliczanie)
	if (activeQuest) {
		return (
			<div className="quest-container">
				<div
					className="quest-active-background"
					style={{
						backgroundImage: `url(/mission_backgrounds/${activeQuest.imagePath})`,
					}}
				>
					<div className="quest-active-overlay">
						<h2 className="quest-active-title">{activeQuest.questTitle}</h2>
						<p className="quest-timer-label">Czas pozostały</p>
						<p className="quest-timer">{timeLeft}</p>
						<p className="quest-timer-hint">
							Zlecenie zakończy się automatycznie – sprawdź później nagrody!
						</p>
						<div className="quest-progress-container">
							<div
								className="quest-progress-fill"
								style={{ width: `${progress}%` }}
							/>
						</div>
					</div>
				</div>
			</div>
		);
	}

	// EKRAN WYBORU QUESTA
	return (
		<div className="quest-container">
			<p className="quest-subtitle">Wybierz jedno zlecenie</p>

			{quests.length === 0 ? (
				<p className="quest-empty">Brak dostępnych zleceń. Zajrzyj później.</p>
			) : (
				<div className="quest-grid">
					{quests.map((quest) => (
						<div
							key={quest.questId}
							className={`quest-card quest-card--${quest.questTier.toLowerCase()}`}
						>
							<div className="quest-card-header">
								<span className="quest-tier-badge">{quest.questTier}</span>
								<span className="quest-type">{quest.questType}</span>
							</div>
							<h3 className="quest-title">{quest.questTitle}</h3>
							<p className="quest-description">{quest.questDescription}</p>
							{/* //trzeba dodać limit znaków dla opsiu questa,aby nie wychodził poza kartę questa we frontendzie */}
							<div className="quest-opponent">
								<span>Konkurent:</span>
								<span className="opponent-name">{quest.opponentName}</span>
							</div>
							<div className="quest-rewards">
								<span>💰 {quest.moneyReward}</span>
								<span>✨ {quest.auraReward}</span>
							</div>
							<button
								className="quest-btn"
								onClick={() => handleSelectQuest(quest.questId)}
							>
								Wybierz
							</button>
						</div>
					))}
				</div>
			)}
		</div>
	);
}

export default Barman;
