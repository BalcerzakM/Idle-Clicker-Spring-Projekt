import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import type { Variants } from "framer-motion";
import "../css/ArenaView.css";
import "../css/TooltipView.css";
import Fist from "../assets/other/fist.png";
import { itemTooltip, type ItemDto } from "../utils/ItemTooltip";

// ----- DTO -----
interface CharacterInBattleDto {
	name: string;
	avatarPicture: string;
	maxHp: number;
}

interface GangCombatDto {
	combatLog: number[];
	teamAWon: boolean;
	teamAImageFolder: string;
	teamBImageFolder: string;
	teamACharacters: CharacterInBattleDto[];
	teamBCharacters: CharacterInBattleDto[];
	moneyReward: number;
	auraReward: number;
	itemReward: ItemDto | null;
}

interface GangArenaProps {
	combatData: GangCombatDto;
	onClose: () => void;
}

type AnimationState = "idle" | "teamAAttack" | "teamBAttack";

const fistVariants: Variants = {
	idle: { x: 0, opacity: 0, scaleX: 1, scale: 1 },
	teamAAttack: {
		x: [0, 200, 280],
		scale: [1, 2, 2],
		opacity: [0, 1, 0],
		scaleX: [1, 1, 1],
		transition: { duration: 0.6, ease: "easeInOut" },
	},
	teamBAttack: {
		x: [0, -200, -280],
		scale: [1, 2, 2],
		opacity: [0, 1, 0],
		scaleX: [-1, -1, -1],
		transition: { duration: 0.6, ease: "easeInOut" },
	},
};

function GangArena({ combatData, onClose }: GangArenaProps) {
	const { teamACharacters, teamBCharacters, combatLog } = combatData;

	// Indeksy aktualnie walczących postaci
	const [indexA, setIndexA] = useState(0);
	const [indexB, setIndexB] = useState(0);

	// Aktualne HP i max HP dla walczących postaci
	const [hpA, setHpA] = useState(teamACharacters[0]?.maxHp ?? 0);
	const [hpB, setHpB] = useState(teamBCharacters[0]?.maxHp ?? 0);
	const [maxHpA, setMaxHpA] = useState(teamACharacters[0]?.maxHp ?? 0);
	const [maxHpB, setMaxHpB] = useState(teamBCharacters[0]?.maxHp ?? 0);

	const [currentStep, setCurrentStep] = useState(0);
	const [animationState, setAnimationState] = useState<AnimationState>("idle");
	const [isFinished, setIsFinished] = useState(false);
	const [currentDamageText, setCurrentDamageText] = useState<number | null>(
		null,
	);

	// Efekt animacji
	useEffect(() => {
		if (currentStep >= combatLog.length) {
			setIsFinished(true);
			return;
		}
		const timerDelay = 800;
		const timer = setTimeout(() => {
			const isTeamA = currentStep % 2 === 0;
			setAnimationState(isTeamA ? "teamAAttack" : "teamBAttack");
		}, timerDelay);
		return () => clearTimeout(timer);
	}, [currentStep, combatLog.length]);

	// Obsługa zakończenia animacji ataku
	const handleAttackComplete = (variant: AnimationState) => {
		if (variant === "idle") return;

		setAnimationState("idle");
		const damage = combatLog[currentStep];
		setCurrentDamageText(damage);

		// Kto atakuje? parzyste -> atak A, nieparzyste -> atak B
		const attackerIsA = currentStep % 2 === 0;
		// Obrażenia otrzymuje przeciwna drużyna
		if (attackerIsA) {
			// Aktualizuj HP drużyny B
			setHpB((prev) => {
				const newHp = prev - damage;
				if (newHp <= 0) {
					// Przejście do następnej postaci w drużynie B
					setIndexB((prevIdx) => {
						const nextIdx = prevIdx + 1;
						if (nextIdx < teamBCharacters.length) {
							const nextChar = teamBCharacters[nextIdx];
							setHpB(nextChar.maxHp);
							setMaxHpB(nextChar.maxHp);
						} else {
							// Wszystkie postacie B pokonane – koniec walki
							setIsFinished(true);
						}
						return nextIdx;
					});
					return 0; // HP spada do 0
				}
				return newHp;
			});
		} else {
			// Aktualizuj HP drużyny A
			setHpA((prev) => {
				const newHp = prev - damage;
				if (newHp <= 0) {
					setIndexA((prevIdx) => {
						const nextIdx = prevIdx + 1;
						if (nextIdx < teamACharacters.length) {
							const nextChar = teamACharacters[nextIdx];
							setHpA(nextChar.maxHp);
							setMaxHpA(nextChar.maxHp);
						} else {
							setIsFinished(true);
						}
						return nextIdx;
					});
					return 0;
				}
				return newHp;
			});
		}

		setCurrentStep((prev) => prev + 1);
		setTimeout(() => setCurrentDamageText(null), 500);
	};

	// Aktualnie wyświetlane postacie
	const currentA = teamACharacters[indexA];
	const currentB = teamBCharacters[indexB];

	const hpPercentA = maxHpA > 0 ? Math.max(0, (hpA / maxHpA) * 100) : 0;
	const hpPercentB = maxHpB > 0 ? Math.max(0, (hpB / maxHpB) * 100) : 0;

	return (
		<div className="arena-container">
			<div className="combat-container">
				{/* DRUŻYNA A */}
				<div className="character-box">
					<h3>Twój gang</h3>
					{currentA && (
						<>
							<img
								src={`/avatars/${currentA.avatarPicture}.png`}
								alt={currentA.name}
								className="arena-avatar"
								onError={(e) => {
									(e.target as HTMLImageElement).src = "/avatars/avatar1.png";
								}}
							/>
							<p className="character-name">{currentA.name}</p>
							<div className="hp-bar">
								<div
									className="hp-fill player-hp"
									style={{ width: `${hpPercentA}%` }}
								></div>
							</div>
							<p className="hp-text">
								HP: {hpA} / {maxHpA}
							</p>
						</>
					)}
				</div>

				{/* CENTRUM */}
				<div className="arena-center">
					{currentDamageText !== null && (
						<motion.div
							className="damage-text"
							initial={{ y: 20, opacity: 0 }}
							animate={{ y: -20, opacity: 1 }}
							exit={{ opacity: 0 }}
						>
							-{currentDamageText}
						</motion.div>
					)}

					<motion.img
						key={currentStep}
						src={Fist}
						alt="Atak"
						className="flying-fist"
						variants={fistVariants}
						initial="idle"
						animate={animationState}
						onAnimationComplete={handleAttackComplete}
					/>
				</div>

				{/* DRUŻYNA B */}
				<div className="character-box">
					<h3>Gang przeciwny</h3>
					{currentB && (
						<>
							<img
								src={`/avatars/${currentB.avatarPicture}.png`}
								alt={currentB.name}
								className="arena-avatar"
								onError={(e) => {
									(e.target as HTMLImageElement).src = "/avatars/avatar1.png";
								}}
							/>
							<p className="character-name">{currentB.name}</p>
							<div className="hp-bar">
								<div
									className="hp-fill enemy-hp"
									style={{ width: `${hpPercentB}%` }}
								></div>
							</div>
							<p className="hp-text">
								HP: {hpB} / {maxHpB}
							</p>
						</>
					)}
				</div>
			</div>

			{/* WYNIKI */}
			{isFinished && (
				<div className="arena-results">
					{combatData.teamAWon ? (
						<>
							<h2 className="victory-text">ZWYCIĘSTWO!</h2>
							<div className="rewards-box">
								<p>
									Złoto: <span>+{combatData.moneyReward}</span>
								</p>
								<p>
									Aura: <span>+{combatData.auraReward}</span>
								</p>
								{combatData.itemReward && (
									<div className="reward-item">
										<p>Zdobywasz łup!</p>
										<div className="reward-item-icon">
											<img
												src={`/items/${combatData.itemReward.imagePath}`}
												alt="Łup"
												className="item-icon"
											/>
											<span className="tooltip">
												{itemTooltip(combatData.itemReward)}
											</span>
										</div>
									</div>
								)}
							</div>
						</>
					) : (
						<h2 className="defeat-text">PORAŻKA!</h2>
					)}
					<button className="quest-btn" onClick={onClose}>
						Wróć do gangu
					</button>
				</div>
			)}
		</div>
	);
}

export default GangArena;
