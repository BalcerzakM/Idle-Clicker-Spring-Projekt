import { useState, useEffect, useCallback } from "react";
import HeroPanel from "../components/HeroPanel";
import "../css/PlayerView.css";
import type { ItemsAndStatsDto } from "../components/HeroPanel";
import PremiumCurrencyImg from "../assets/other/currency_premium.png";
import { useAlert } from "../context/AlertContext";
import { useCharacter } from "../context/CharacterContext";

/** Statystyki możliwe do ulepszenia */
const UPGRADEABLE_STATS = [
	{ key: "RIZZ", label: "✨ Rizz", field: "totalRizz" as const },
	{ key: "STRENGTH", label: "💪 Siła", field: "totalStrength" as const },
	{ key: "AGILITY", label: "🏃 Zwinność", field: "totalAgility" as const },
	{ key: "ENDURANCE", label: "🛡️ Wytrz.", field: "totalEndurance" as const },
	{ key: "LUCK", label: "🍀 Szczęście", field: "totalLuck" as const },
];

function Player() {
	const { showError } = useAlert();
	const { refreshCharacter } = useCharacter();

	const [hero, setHero] = useState<ItemsAndStatsDto | null>(null);
	const [loading, setLoading] = useState(true);
	const [highlightedSlot, setHighlightedSlot] = useState<string | null>(null);

	/** Pobranie rozszerzonych danych postaci */
	const fetchPlayerDetails = useCallback(async () => {
		try {
			setLoading(true);
			const res = await fetch("http://localhost:8080/api/character/statsItems");
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Nie udało się pobrać danych");
				return;
			}
			setHero(await res.json());
		} catch (err) {
			console.error(err);
			showError("Brak połączenia z serwerem");
		} finally {
			setLoading(false);
		}
	}, [showError]);

	useEffect(() => {
		fetchPlayerDetails();
	}, [fetchPlayerDetails]);

	/** Zwiększenie statystyki (PATCH) */
	const handleIncrement = async (statName: string) => {
		try {
			const res = await fetch(
				`http://localhost:8080/api/character/statIncrement?stat=${statName}`,
				{ method: "PATCH" },
			);
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Nie udało się zwiększyć statystyki");
				return;
			}
			await fetchPlayerDetails();
			await refreshCharacter();
		} catch (err) {
			console.error(err);
			showError("Brak połączenia z serwerem");
		}
	};

	/** Equip (drag & drop) */
	const handleEquip = async (backpackItemId: number) => {
		if (!hero) return;
		const item = hero.backpack.find((i) => i.id === backpackItemId);
		if (!item) return;

		const equippedId =
			hero.equipment.find((e) => e.slotType === item.slotType)?.id ?? null;

		try {
			const res = await fetch("http://localhost:8080/api/character/equip", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ equipmentItemId: equippedId, backpackItemId }),
			});
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Nie udało się założyć przedmiotu");
				return;
			}
			await fetchPlayerDetails();
		} catch (err) {
			console.error(err);
			showError("Brak połączenia z serwerem");
		}
	};

	const handleHoverSlot = (slotType: string | null) =>
		setHighlightedSlot(slotType);

	if (loading && !hero) {
		return <div className="player-loading">Ładowanie...</div>;
	}

	return (
		<div className="player-container">
			{/* LEWA STRONA – HeroPanel */}
			{hero && (
				<HeroPanel
					hero={hero}
					onSell={() => {}}
					onEquip={handleEquip}
					highlightedSlot={highlightedSlot}
					onHoverSlot={handleHoverSlot}
				/>
			)}

			{/* PRAWA STRONA */}
			<div className="player-panel">
				<div className="player-header">
					<h2 className="player-name">{hero?.name}</h2>
				</div>

				<div className="player-stats">
					<h3>Statystyki</h3>
					{UPGRADEABLE_STATS.map((stat) => (
						<div key={stat.key} className="player-stat-row">
							<span className="stat-label">{stat.label}</span>
							<span className="stat-value">{hero?.[stat.field] ?? 0}</span>
							<button
								className="stat-upgrade-btn"
								onClick={() => handleIncrement(stat.key)}
							>
								+{" "}
								<img src={PremiumCurrencyImg} alt="💎" width={14} height={14} />
							</button>
						</div>
					))}
				</div>

				{/* Okno pojazdu */}
				<div className="vehicle-slot">
					<h3>Pojazd</h3>
					<div className="vehicle-placeholder">Brak pojazdu</div>
				</div>
			</div>
		</div>
	);
}

export default Player;
