import "../css/BossView.css";
import questCard from "../assets/scenes/other/special_quest_card.png";
import {useCallback, useEffect, useState} from "react";
import Arena from "../components/Arena";
import {useAlert} from "../context/AlertContext";
import {useCharacter} from "../context/CharacterContext";

interface SpecialQuestDto {
    questTitle: string;
    questDescription: string;
    questType: string;
    opponentName: string;
    opponentImagePath: string;
}

function Boss() {
    const { showError } = useAlert();
    const { refreshCharacter } = useCharacter();

    const [bossQuest, setBossQuest] = useState<SpecialQuestDto | null>(null);
    const [combatResult, setCombatResult] = useState<any | null>(null);
    const [loading, setLoading] = useState(true);
    const [startingCombat, setStartingCombat] = useState(false);
    const [noBoss, setNoBoss] = useState(false);

    const fetchBossQuest = useCallback(async () => {
        try {
            setLoading(true);

            const res = await fetch("http://localhost:8080/api/boss");

            if (!res.ok) {
                const error = await res.json();
                if (res.status === 404) {
                    setBossQuest(null);
                    setNoBoss(true);
                    return;
                }
                showError(error.message || "Nie udało się pobrać bossa");
                return;
            }

            const data: SpecialQuestDto = await res.json();
            setBossQuest(data);
        } catch (err) {
            console.error(err);
            showError("Brak połączenia z serwerem");
        } finally {
            setLoading(false);
        }
    }, [showError]);

    useEffect(() => {
        fetchBossQuest();
    }, [fetchBossQuest]);

    const handleStartBossCombat = async () => {
        try {
            setStartingCombat(true);

            const res = await fetch("http://localhost:8080/api/boss/combat", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                }
            });

            if (!res.ok) {
                const error = await res.json();
                showError(error.message || "Nie udało się rozpocząć walki");
                return;
            }

            const data = await res.json();
            setCombatResult(data);
        } catch (err) {
            console.error(err);
            showError("Brak połączenia z serwerem");
        } finally {
            setStartingCombat(false);
        }
    };

    if (loading) {
        return <div className="boss-loading">Ładowanie...</div>;
    }

    if (combatResult) {
        return (
            <Arena
                combatData={combatResult}
                onClose={() => {
                    setCombatResult(null);
                    refreshCharacter();
                    fetchBossQuest();
                }}
            />
        );
    }

    return (
        <div className="boss">
            <div className="boss-card">
                <img
                    src={questCard}
                    alt="boss_card"
                    width={735}
                    height={400}
                    className="boss-card-img"
                />
                {noBoss && (
                    <div className="boss-card-content boss-card-empty">
                        <div className="boss-info">
                            <h2>Brak zleceń specjalnych</h2>
                            <p className="boss-description">
                                Wróć później
                            </p>
                        </div>
                    </div>
                )}

                {bossQuest && (
                    <div className="boss-card-content">
                        <div className="boss-opponent">
                            <img
                                src={`/opponents/${bossQuest.opponentImagePath}`}
                                alt={bossQuest.opponentName}
                                className="boss-opponent-img"
                            />
                            <p className="boss-opponent-name">
                                Cel: <span>{bossQuest.opponentName}</span>
                            </p>
                        </div>

                        <div className="boss-info">
                            <h2>{bossQuest.questTitle}</h2>

                            <p className="boss-description">
                                {bossQuest.questDescription}
                            </p>

                            <p className="boss-type">
                                Typ walki: <span>{bossQuest.questType}</span>
                            </p>
                            <button
                                className="boss-fight-btn"
                                onClick={handleStartBossCombat}
                                disabled={startingCombat}
                            >
                                {startingCombat ? "Start..." : "PRZYJMIJ ZLECENIE"}
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Boss;