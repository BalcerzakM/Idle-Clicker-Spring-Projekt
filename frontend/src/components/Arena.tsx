import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import type {Variants} from 'framer-motion';
import { useCharacter } from '../context/CharacterContext';
import '../css/Arena.css';
import Fist from "../assets/other/fist.png";

interface CombatDto {
    combatLog: number[];
    playerWon: boolean;
    playerHp: number;
    enemyHp: number;
    enemyName: string;
    enemyImagePath: string;
    moneyReward: number;
    auraReward: number;
    itemRewardImagePath: string | null;
}

interface ArenaProps {
    combatData: CombatDto;
    onClose: () => void;
}

type AnimationState = 'idle' | 'playerAttack' | 'enemyAttack';

function Arena({ combatData, onClose }: ArenaProps) {
    const { character } = useCharacter();

    const [currentStep, setCurrentStep] = useState(0);
    const [animationState, setAnimationState] = useState<AnimationState>('idle');
    const [isFinished, setIsFinished] = useState(false);
    const [currentDamageText, setCurrentDamageText] = useState<number | null>(null);

    const [currentPlayerHp, setCurrentPlayerHp] = useState(combatData.playerHp);
    const [currentEnemyHp, setCurrentEnemyHp] = useState(combatData.enemyHp);

    const maxPlayerHp = combatData.playerHp;
    const maxEnemyHp = combatData.enemyHp;

    const fistVariants: Variants = {
        idle: { x: 0, opacity: 0, scaleX: 1 , scale: 1 },
        playerAttack: {
            x: [0, 280, 280],
            scale: [1, 2, 2], 
            opacity: [0, 1, 0], 
            scaleX: [1, 1, 1],
            transition: { duration: 0.6, ease: "easeInOut" }
        },
        enemyAttack: {
            x: [0, -280, -280],
            scale: [1, 2, 2], 
            opacity: [0, 1, 0],
            scaleX: [-1, -1, -1], 
            transition: { duration: 0.6, ease: "easeInOut" }
        }
    };

    useEffect(() => {
        if (currentStep >= combatData.combatLog.length) {
            setIsFinished(true);
            return;
        }

        const timer = setTimeout(() => {
            const isPlayerTurn = currentStep % 2 === 0;
            setAnimationState(isPlayerTurn ? 'playerAttack' : 'enemyAttack');
        }, 1000);

        return () => clearTimeout(timer);
    }, [currentStep, combatData.combatLog.length]);

    const handleAttackComplete = (variant: any) => {
        if (variant === 'idle') return;

        setAnimationState('idle');
        
        const damage = combatData.combatLog[currentStep];
        setCurrentDamageText(damage); 

        if (currentStep % 2 === 0) {
            setCurrentEnemyHp(prev => prev - damage);
        } else {
            setCurrentPlayerHp(prev => prev - damage);
        }

        setCurrentStep(prev => prev + 1);
        setTimeout(() => setCurrentDamageText(null), 500);
    };
    const playerHpPercent = Math.max(0, (currentPlayerHp / maxPlayerHp) * 100);
    const enemyHpPercent = Math.max(0, (currentEnemyHp / maxEnemyHp) * 100);

    return (
        <div className="arena-container">
            <div className="combat-container">
                <div className="character-box">
                    <h3>Ty</h3>
                    <img src={`/avatars/${character?.avatarPicture}`} alt="Gracz" className="arena-avatar" />
                    <div className="hp-bar">
                        <div className="hp-fill player-hp" style={{ width: `${playerHpPercent}%` }}></div>
                    </div>
                    <p className="hp-text">HP: {currentPlayerHp} / {maxPlayerHp}</p>
                </div>
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
                        src={Fist} 
                        alt="Pięść" 
                        className="flying-fist"
                        variants={fistVariants}
                        initial="idle"
                        animate={animationState}
                        onAnimationComplete={handleAttackComplete} 
                    />
                </div>
                <div className="character-box">
                    <h3>{combatData.enemyName}</h3>
                    <img src={`/opponents/${combatData.enemyImagePath}`} alt="Wróg" className="arena-avatar" />
                    <div className="hp-bar">
                        <div className="hp-fill enemy-hp" style={{ width: `${enemyHpPercent}%` }}></div>
                    </div>
                    <p className="hp-text">HP: {currentEnemyHp} / {maxEnemyHp}</p>
                </div>
            </div>

            {isFinished && (
                <div className="arena-results">
                    {combatData.playerWon ? (
                        <>
                            <h2 className="victory-text">ZWYCIĘSTWO!</h2>
                            <div className="rewards-box">
                                <p>Złoto: <span>+{combatData.moneyReward}</span></p>
                                <p>Aura: <span>+{combatData.auraReward}</span></p>
                                {combatData.itemRewardImagePath && (
                                    <div className="reward-item">
                                        <p>Zdobywasz łup!</p>
                                        <img src={`/items/${combatData.itemRewardImagePath}`} alt="Łup" />
                                    </div>
                                )}
                            </div>
                        </>
                    ) : (
                        <h2 className="defeat-text">PORAŻKA!</h2>
                    )}
                    <button className="quest-btn" onClick={onClose}>
                        Wróć do Baru
                    </button>
                </div>
            )}
        </div>
    );
}

export default Arena;