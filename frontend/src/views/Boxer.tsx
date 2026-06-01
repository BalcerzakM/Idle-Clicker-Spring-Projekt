import { useState } from "react";

interface BoxerResultDto {
    result: number;
    winAmount: number;
    bet: number;
    lucky: boolean;
}

function Boxer() {
    const [bet, setBet] = useState<number>(10);
    const [displayScore, setDisplayScore] = useState<number>(0);
    const [finalResult, setFinalResult] = useState<BoxerResultDto | null>(null);
    const [playing, setPlaying] = useState(false);

    const animateScore = (target: number) => {
        const duration = 2500;
        const start = performance.now();

        const tick = (now: number) => {
            const progress = Math.min((now - start) / duration, 1);

            const eased = 1 - Math.pow(1 - progress, 3);

            const value = Math.floor(eased * target);
            setDisplayScore(value);

            if (progress < 1) {
                requestAnimationFrame(tick);
            } else {
                setDisplayScore(target);
                setPlaying(false);
            }
        }
        requestAnimationFrame(tick);
    }

    const playBoxer = async () => {
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
                throw new Error("Nie udalo sie zagrac w boxera");
            }

            const data: BoxerResultDto = await res.json();
            setFinalResult(data);

            animateScore(data.result);
        } catch (e) {
            console.error(e);
            setPlaying(false);
        }
    }

    return (
        <div className="outsideBoxer">
            <div className="boxer-score-display">
                {displayScore.toString().padStart(4, "0")}
            </div>

            <div className="boxer-credit-display">
                <input
                    value={bet}
                    onChange={(e) => setBet(Number(e.target.value))}
                    type="number"
                    min={1}
                    disabled={playing}
                />
            </div>

            <button
                className="boxer-hit-button"
                onClick={playBoxer}
                disabled={playing}
            />

            {finalResult && !playing && (
                <div className="boxer-result-text">
                    Wygrana: {finalResult.winAmount}
                </div>
            )}
        </div>
    );
}

export default Boxer;