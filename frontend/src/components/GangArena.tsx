import { useState, useEffect, useReducer, useCallback } from "react";
import { motion } from "framer-motion";
import type { Variants } from "framer-motion";
import "../css/ArenaView.css";
import "../css/TooltipView.css";
import Fist from "../assets/other/fist.png";
import Kiss from "../assets/other/kiss.png";
import { itemTooltip, type ItemDto } from "../utils/ItemTooltip";

// ----- DTO -----
interface CharacterInBattleDto {
	characterName: string;
	imagePath: string;
	hp: number;
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
type ProjectileType = "fist" | "rizz";

// ----- Stan walki (reducer) -----
interface BattleState {
	indexA: number;
	indexB: number;
	hpA: number;
	hpB: number;
	maxHpA: number;
	maxHpB: number;
	currentLogIdx: number;
	attackerIsTeamA: boolean;
	stepInPair: number;
	isFinished: boolean;
	isTransitioning: boolean; // true podczas oczekiwania na wejście nowej postaci
	pendingNextChar: {
		side: "A" | "B";
		nextIndex: number;
		nextHp: number;
		nextMaxHp: number;
	} | null;
}

type BattleAction =
	| { type: "INIT"; payload: GangCombatDto }
	| { type: "APPLY_DAMAGE"; damage: number; combatData: GangCombatDto }
	| { type: "TRANSITION_COMPLETE" }
	| { type: "FINISH" };

function createInitialState(combatData: GangCombatDto): BattleState {
	const firstA = combatData.teamACharacters[0];
	const firstB = combatData.teamBCharacters[0];
	return {
		indexA: 0,
		indexB: 0,
		hpA: firstA.hp,
		hpB: firstB.hp,
		maxHpA: firstA.hp,
		maxHpB: firstB.hp,
		currentLogIdx: 0,
		attackerIsTeamA: true,
		stepInPair: 0,
		isFinished: false,
		isTransitioning: false,
		pendingNextChar: null,
	};
}

function battleReducer(state: BattleState, action: BattleAction): BattleState {
	switch (action.type) {
		case "INIT":
			return createInitialState(action.payload);

		case "APPLY_DAMAGE": {
			if (state.isTransitioning) return state;

			const { damage, combatData } = action;
			let {
				indexA,
				indexB,
				hpA,
				hpB,
				attackerIsTeamA,
				stepInPair,
				currentLogIdx,
			} = state;

			// zadaj obrażenia
			if (attackerIsTeamA) {
				hpB = Math.max(0, hpB - damage);
			} else {
				hpA = Math.max(0, hpA - damage);
			}

			const defenderDied =
				(attackerIsTeamA && hpB <= 0) || (!attackerIsTeamA && hpA <= 0);

			// zawsze zwiększamy licznik logów, przełączamy atakującego i krok
			const nextCurrentLogIdx = currentLogIdx + 1;
			const nextAttackerIsTeamA = !attackerIsTeamA;
			const nextStepInPair = stepInPair + 1;

			if (defenderDied) {
				let pendingNextChar: BattleState["pendingNextChar"] = null;

				if (attackerIsTeamA) {
					const nextIdxB = indexB + 1;
					if (nextIdxB < combatData.teamBCharacters.length) {
						const nextChar = combatData.teamBCharacters[nextIdxB];
						pendingNextChar = {
							side: "B",
							nextIndex: nextIdxB,
							nextHp: nextChar.hp,
							nextMaxHp: nextChar.hp,
						};
					} else {
						// brak kolejnych postaci -> koniec
						return {
							...state,
							hpA,
							hpB,
							currentLogIdx: nextCurrentLogIdx,
							attackerIsTeamA: nextAttackerIsTeamA,
							stepInPair: nextStepInPair,
							isFinished: true,
						};
					}
				} else {
					const nextIdxA = indexA + 1;
					if (nextIdxA < combatData.teamACharacters.length) {
						const nextChar = combatData.teamACharacters[nextIdxA];
						pendingNextChar = {
							side: "A",
							nextIndex: nextIdxA,
							nextHp: nextChar.hp,
							nextMaxHp: nextChar.hp,
						};
					} else {
						return {
							...state,
							hpA,
							hpB,
							currentLogIdx: nextCurrentLogIdx,
							attackerIsTeamA: nextAttackerIsTeamA,
							stepInPair: nextStepInPair,
							isFinished: true,
						};
					}
				}

				return {
					...state,
					hpA,
					hpB,
					currentLogIdx: nextCurrentLogIdx,
					attackerIsTeamA: nextAttackerIsTeamA,
					stepInPair: nextStepInPair,
					isTransitioning: true,
					pendingNextChar,
				};
			} else {
				// normalny przypadek – brak śmierci
				return {
					...state,
					hpA,
					hpB,
					currentLogIdx: nextCurrentLogIdx,
					attackerIsTeamA: nextAttackerIsTeamA,
					stepInPair: nextStepInPair,
				};
			}
		}

		case "TRANSITION_COMPLETE": {
			if (!state.pendingNextChar) return state;

			const { side, nextIndex, nextHp, nextMaxHp } = state.pendingNextChar;
			const newState: BattleState = {
				...state,
				isTransitioning: false,
				pendingNextChar: null,
				stepInPair: 0, // reset pary przy nowej postaci
				attackerIsTeamA: state.attackerIsTeamA, // atakujący nie zmienia się (ten który zabił)
			};

			if (side === "A") {
				newState.indexA = nextIndex;
				newState.hpA = nextHp;
				newState.maxHpA = nextMaxHp;
			} else {
				newState.indexB = nextIndex;
				newState.hpB = nextHp;
				newState.maxHpB = nextMaxHp;
			}
			return newState;
		}

		case "FINISH":
			return { ...state, isFinished: true };

		default:
			return state;
	}
}

// ----- Warianty animacji -----
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

const rizzVariants: Variants = {
	idle: { x: 0, y: 0, opacity: 0, scaleX: 1, scale: 1, rotate: 0 },
	teamAAttack: {
		x: [0, 180, 280],
		y: [0, -40, 0],
		rotate: [0, 15, -10, 0],
		scale: [0.5, 1.8, 3],
		opacity: [0, 1, 0],
		scaleX: [-1, -1, -1],
		transition: { duration: 0.9, ease: "easeOut" },
	},
	teamBAttack: {
		x: [0, -180, -280],
		y: [0, -40, 0],
		rotate: [0, -15, 10, 0],
		scale: [0.5, 1.8, 3],
		opacity: [0, 1, 0],
		scaleX: [1, 1, 1],
		transition: { duration: 0.9, ease: "easeOut" },
	},
};

function GangArena({ combatData, onClose }: GangArenaProps) {
	const [state, dispatch] = useReducer(
		battleReducer,
		combatData,
		createInitialState,
	);

	const [animationState, setAnimationState] = useState<AnimationState>("idle");
	const [currentDamageText, setCurrentDamageText] = useState<number | null>(
		null,
	);

	// Obsługa opóźnienia po śmierci postaci – gdy isTransitioning, po 600ms wysyłamy TRANSITION_COMPLETE
	useEffect(() => {
		if (state.isTransitioning && !state.isFinished) {
			const timer = setTimeout(() => {
				dispatch({ type: "TRANSITION_COMPLETE" });
			}, 600); // czas na animację paska HP (0.3s) + chwila na pokazanie 0
			return () => clearTimeout(timer);
		}
	}, [state.isTransitioning, state.isFinished]);

	// Typ pocisku – globalny licznik ataków (currentLogIdx) niezależny od wejść nowych postaci
	// Wzór: x / 2 % 2 == 0 -> pięść, inaczej buziak (fist, fist, kiss, kiss, ...)
	const projectileType: ProjectileType =
		Math.floor(state.currentLogIdx / 2) % 2 === 0 ? "fist" : "rizz";
	const activeVariants =
		projectileType === "fist" ? fistVariants : rizzVariants;
	const activeProjectile = projectileType === "fist" ? Fist : Kiss;

	// Rozpoczęcie animacji ataku
	useEffect(() => {
		if (
			state.isFinished ||
			state.isTransitioning || // blokujemy ataki podczas przejścia
			state.currentLogIdx >= combatData.combatLog.length
		) {
			if (
				!state.isFinished &&
				state.currentLogIdx >= combatData.combatLog.length
			) {
				dispatch({ type: "FINISH" });
			}
			return;
		}

		const timerDelay = projectileType === "rizz" ? 1100 : 800;
		const timer = setTimeout(() => {
			setAnimationState(state.attackerIsTeamA ? "teamAAttack" : "teamBAttack");
		}, timerDelay);
		return () => clearTimeout(timer);
	}, [
		state.currentLogIdx,
		state.isFinished,
		state.isTransitioning,
		state.attackerIsTeamA,
		projectileType,
	]);

	const handleAttackComplete = useCallback(
		(variant: AnimationState) => {
			if (variant === "idle") return;
			setAnimationState("idle");

			const damage = combatData.combatLog[state.currentLogIdx];
			setCurrentDamageText(damage);

			dispatch({ type: "APPLY_DAMAGE", damage, combatData });

			setTimeout(() => setCurrentDamageText(null), 500);
		},
		[state.currentLogIdx, combatData],
	);

	// Aktualne postacie – nawet podczas przejścia pokazujemy jeszcze starą (z hp=0), aż do TRANSITION_COMPLETE
	const currentA = combatData.teamACharacters[state.indexA];
	const currentB = combatData.teamBCharacters[state.indexB];

	const hpPercentA =
		state.maxHpA > 0 ? Math.max(0, (state.hpA / state.maxHpA) * 100) : 0;
	const hpPercentB =
		state.maxHpB > 0 ? Math.max(0, (state.hpB / state.maxHpB) * 100) : 0;

	return (
		<div className="arena-container">
			<div className="combat-container">
				{/* DRUŻYNA A */}
				<div className="character-box">
					<h3>Twój gang</h3>
					{currentA && (
						<>
							<img
								src={`/avatars/${currentA.imagePath}`}
								alt={currentA.characterName}
								className="arena-avatar"
								onError={(e) => {
									(e.target as HTMLImageElement).src = "/avatars/avatar1.png";
								}}
							/>
							<p className="character-name">{currentA.characterName}</p>
							<div className="hp-bar">
								<div
									className="hp-fill player-hp"
									style={{ width: `${hpPercentA}%` }}
								></div>
							</div>
							<p className="hp-text">
								HP: {state.hpA} / {state.maxHpA}
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

					{/* Podczas przejścia nie pokazujemy nowego pocisku – atak już był */}
					{!state.isTransitioning && (
						<motion.img
							key={state.currentLogIdx}
							src={activeProjectile}
							alt={projectileType === "fist" ? "Pięść" : "Buziak"}
							className="flying-fist"
							variants={activeVariants}
							initial="idle"
							animate={animationState}
							onAnimationComplete={handleAttackComplete}
						/>
					)}
				</div>

				{/* DRUŻYNA B */}
				<div className="character-box">
					<h3>Gang przeciwny</h3>
					{currentB && (
						<>
							<img
								src={`/avatars/${currentB.imagePath}`}
								alt={currentB.characterName}
								className="arena-avatar"
								onError={(e) => {
									(e.target as HTMLImageElement).src = "/avatars/avatar1.png";
								}}
							/>
							<p className="character-name">{currentB.characterName}</p>
							<div className="hp-bar">
								<div
									className="hp-fill enemy-hp"
									style={{ width: `${hpPercentB}%` }}
								></div>
							</div>
							<p className="hp-text">
								HP: {state.hpB} / {state.maxHpB}
							</p>
						</>
					)}
				</div>
			</div>

			{/* WYNIKI */}
			{state.isFinished && (
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
