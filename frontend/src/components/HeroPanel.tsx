import "../css/HeroPanel.css";
import "../css/TooltipView.css";
import { itemTooltip, type ItemDto } from "../utils/ItemTooltip";
import {useEffect, useState} from "react";

export interface EffectDto {
    item: ItemDto;
    effectStartTime: string;
    effectEndTime: string;
}

export interface ItemsAndStatsDto {
	name: string;
	avatarPicture: string;
	aura: number;
	auraLvl: number;
	totalRizz: number;
	totalStrength: number;
	totalAgility: number;
	totalEndurance: number;
	totalLuck: number;

	backpack: ItemDto[];
	equipment: ItemDto[];
    effects: EffectDto[];
}

interface HeroPanelProps {
	hero: ItemsAndStatsDto;
	onUseItem: (backpackItemId: number) => void;
	highlightedSlot: string | null;
	onHoverSlot: (slotType: string | null) => void;
}

function HeroPanel({
	hero,
	onUseItem,
	highlightedSlot,
	onHoverSlot,
}: HeroPanelProps) {
	const handleDragStart = (
		e: React.DragEvent<HTMLDivElement>,
		backpackItemId: number,
	) => {
		e.dataTransfer.setData("text/plain", String(backpackItemId));
		e.dataTransfer.effectAllowed = "move";
	};

	const handleDragOver = (e: React.DragEvent<HTMLDivElement>) => {
		e.preventDefault();
		e.dataTransfer.dropEffect = "move";
	};

	const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
		e.preventDefault();
		const backpackItemId = Number(e.dataTransfer.getData("text/plain"));
		if (isNaN(backpackItemId)) return;
		onUseItem(backpackItemId);
	};

    const [now, setNow] = useState(Date.now());

    useEffect(() => {
        const interval = setInterval(() => {
            setNow(Date.now());
        }, 1000);

        return () => clearInterval(interval);
    }, []);

    const formatRemainingTime = (endTime: string) => {
        const diff = new Date(endTime).getTime() - now;

        if (diff <= 0) return "0:00";

        const minutes = Math.floor(diff / 60000);
        const seconds = Math.floor((diff % 60000) / 1000);

        return `${minutes}:${seconds.toString().padStart(2, "0")}`;
    }

	return (
		<div className="hero-panel">
			<div className="hero-stats">
				<span>✨ {hero.totalRizz}</span>
				<span>💪 {hero.totalStrength}</span>
				<span>🏃 {hero.totalAgility}</span>
				<span>🛡️ {hero.totalEndurance}</span>
				<span>🍀 {hero.totalLuck}</span>
			</div>

			<div className="equipment">
				<div
					className="portrait"
					id="nameSlot"
					onDragOver={handleDragOver}
					onDrop={handleDrop}
				>
					<img
						src={`/avatars/${hero.avatarPicture}`}
						alt={hero.name}
						className="portrait-img"
					/>
					<span className="tooltip">{hero.name}</span>
				</div>

				{[
					"HEAD",
					"NECK",
					"UPPER_BODY",
					"LOWER_BODY",
					"FEET",
					"WRIST",
					"EMBLEM",
				].map((slotType) => {
					const item =
						hero.equipment.find((eq) => eq.slotType === slotType) ?? null;
					const isHighlighted = highlightedSlot === slotType;

					return (
						<div
							key={slotType}
							className={`slot equip-slot ${isHighlighted ? "highlight-slot" : ""} ${isHighlighted ? "show-tooltip" : ""}`}
							data-slot={slotType}
							onDragOver={handleDragOver}
							onDrop={handleDrop}
						>
							{item ? (
								<>
									<img
										src={`/items/${item.imagePath}`}
										alt={item.itemName}
										className="item-icon"
									/>
									<span className="tooltip">{itemTooltip(item)}</span>
								</>
							) : (
								<span className="slot-placeholder">{slotType}</span>
							)}
						</div>
					);
				})}
			</div>

			<div className="inventory">
				<h3>Plecak</h3>
				<div id="inventory-grid">
					{(hero.backpack ?? []).map((item) => (
						<div
							key={item.id}
							className="slot backpack-slot"
							draggable
							onDragStart={(e) => handleDragStart(e, item.id)}
							onMouseEnter={() => onHoverSlot(item.slotType)}
							onMouseLeave={() => onHoverSlot(null)}
						>
							<img
								src={`/items/${item.imagePath}`}
								alt={item.itemName}
								className="item-icon"
							/>
							<div className="tooltip">{itemTooltip(item)}</div>
						</div>
					))}
					{Array.from({ length: Math.max(0, 10 - hero.backpack.length) }).map(
						(_, i) => (
							<div key={`empty-${i}`} className="slot empty-slot" />
						),
					)}
				</div>
			</div>

            <div className="effects">
                <div className="effects-grid">
                    {(hero.effects ?? []).map(effect => (
                        <div key={effect.item.id} className="effect-card">
                            <img
                                src={`/items/${effect.item.imagePath}`}
                                alt={effect.item.itemName}
                                className="effect-icon"
                            />
                            <div className="effect-info">
                                <div className="effect-name">
                                    {effect.item.itemName}
                                </div>

                                <div className="effect-bonus">
                                    +{effect.item.totalRizz > 0 && `${effect.item.totalRizz}✨ `}
                                    {effect.item.totalStrength > 0 && `${effect.item.totalStrength}💪 `}
                                    {effect.item.totalAgility > 0 && `${effect.item.totalAgility}🏃 `}
                                    {effect.item.totalEndurance > 0 && `${effect.item.totalEndurance}🛡️ `}
                                    {effect.item.totalLuck > 0 && `${effect.item.totalLuck}🍀 `}
                                </div>
                            </div>

                            <div className="effect-timer">
                                {formatRemainingTime(effect.effectEndTime)}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
		</div>
	);
}

export default HeroPanel;
