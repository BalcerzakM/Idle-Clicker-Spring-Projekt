import { useState } from "react";
import { useCharacter } from "../context/CharacterContext";
import "../css/BoxerView.css";
import { useAlert } from "../context/AlertContext.tsx";
import boxerIdle from "../assets/scenes/boxer/boxer_idle.png";
import boxerHit from "../assets/scenes/boxer/boxer_hit.png";
import boxerIdleHover from "../assets/scenes/boxer/boxer_idle_hover.png";

interface BoxerResultDto {
    result: number;
    winAmount: number;
    bet: number;
    lucky: boolean;
}

function Boxer() {
    const [bet, setBet] = useState(10);
    const [displayScore, setDisplayScore] = useState<number>(0);
    const [finalResult, setFinalResult] = useState<BoxerResultDto | null>(null);
    const [hovered, setHovered] = useState(false);
    const [playing, setPlaying] = useState(false);
    const {refreshCharacter} = useCharacter();
    const {showError} = useAlert();

    const animateScore = (target: number, onFinish?: () => void) => {
        const duration = 5000;
        const start = performance.now();

        const tick = (now: number) => {
            const progress = Math.min((now - start) / duration, 1);

            const eased = 1 - Math.pow(1 - progress, 6);
            //const eased = Math.sin((progress * Math.PI)/2);

            const value = Math.round(eased * target);
            setDisplayScore(value);

            if (progress < 1) {
                requestAnimationFrame(tick);
            } else {
                setDisplayScore(target);
                setPlaying(false);

                if (onFinish) {
                    onFinish();
                }
            }
        }
        requestAnimationFrame(tick);
    }

    const playBoxer = async () => {
        if(playing) {
            return;
        }

        try {
            setPlaying(true);
            setFinalResult(null);
            setDisplayScore(0);

            const res = await fetch("http://localhost:8080/api/boxer/play", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(bet),
                }
            );

            if(!res.ok) {
                const error = await res.json();
                showError(error.message);
                setPlaying(false);
                return;
            }
            const data: BoxerResultDto = await res.json();
            setFinalResult(data);

            animateScore(data.result, () => {
                setPlaying(false);
                refreshCharacter();
            });
        } catch (e) {
            console.error(e);
            showError("Brak połączenia z serwerem")
            setPlaying(false);
        }
    }

    let boxerImage = boxerIdle;

    if (playing) {
        boxerImage = boxerHit;
    } else if (hovered) {
        boxerImage = boxerIdleHover;
    }

    return (
        <div className="outsideBoxer">
            <div className="boxer-score-display">
                {displayScore.toString().padStart(3, "0")}
            </div>

            <div className={`boxer-credit-display ${playing ? "playing" : ""}`}>
                <input
                    value={bet.toString().padStart(4, "0")}
                    onChange={(e) => {
                        const onlyDigits = e.target.value.replace(/\D/g,"");
                        const value = Number(onlyDigits || 0);
                        setBet(Math.min(value, 9999));
                    }}
                    type="text"
                    inputMode={"numeric"}
                    disabled={playing}
                />
            </div>

            <div className="boxer-hit-button">
                <img
                    src={playing ? boxerHit : boxerImage}
                    alt="boxer_idle"
                    width={447}
                    height={708}
                    className="boxer-hit-button-img"
                    onClick={playBoxer}
                    onMouseEnter={() => setHovered(true)}
                    onMouseLeave={() => setHovered(false)}
                />
            </div>

            {finalResult && !playing && (
                <div className="boxer-info-display">
                    Wygrana: {finalResult.winAmount}
                    {finalResult.lucky && (
                        <div className="lucky-hit">
                            LUCKY PUNCH!
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default Boxer;