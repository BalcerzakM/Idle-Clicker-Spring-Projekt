import { useState } from "react";
export interface ItemDto {
	id: number;
	name: string;
	description: string;
	slotType: string;
	totalRizz: number;
	totalStrength: number;
	totalAgility: number;
	totalEndurance: number;
	totalLuck: number;
	price: number;
	imagePath: string;
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
}

interface HeroPanelProps {
	hero: ItemsAndStatsDto;
	onSell: (backpackItemId: number) => void;
	onEquip: (backpackItemId: number, slotType: string) => void;
}

function HeroPanel({ hero, onSell, onEquip }: HeroPanelProps) {
	const [highlightedSlot, setHighlightedSlot] = useState<string | null>(null); //to te podświetlanie konkretnego slotu

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

	const handleDropOnSlot = (
		e: React.DragEvent<HTMLDivElement>,
		slotType: string,
	) => {
		e.preventDefault();

		const backpackItemId = Number(e.dataTransfer.getData("text/plain"));

		if (isNaN(backpackItemId)) {
			return;
		}

		onEquip(backpackItemId, slotType);
	};

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
				{/* Portret */}
				<div className="portrait" id="nameSlot">
					<img
						src={`/avatars/${hero.avatarPicture}`}
						alt={hero.name}
						className="portrait-img"
					/>

					<span className="tooltip">{hero.name} |</span>
				</div>

				{/* Sloty */}
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
						hero.equipment.find((item) => item.slotType === slotType) ?? null;

					const isHighlighted = highlightedSlot === slotType; //sprawdzenie czy ma być podświetlony

					return (
						<div
							key={slotType}
							className={`slot equip-slot ${isHighlighted ? "highlight-slot" : ""}`} //sprawdzenie czy podświetlać
							data-slot={slotType}
							onDragOver={handleDragOver}
							onDrop={(e) => handleDropOnSlot(e, slotType)}
						>
							{item ? (
								<>
									<img
										src={`/items/${item.imagePath}`}
										alt={item.name}
										className="item-icon"
									/>

									<span className="tooltip">
										{item.name} | {item.slotType}
									</span>
								</>
							) : (
								<span className="slot-placeholder">{slotType}</span>
							)}
						</div>
					);
				})}
			</div>

			{/* Plecak */}
			<div className="inventory">
				<h3>Plecak</h3>

				<div id="inventory-grid">
					{hero.backpack.map((item) => (
						<div
							key={item.id}
							className="slot backpack-slot"
							draggable
							onDragStart={(e) => handleDragStart(e, item.id)}
							onMouseEnter={() => setHighlightedSlot(item.slotType)}
							onMouseLeave={() => setHighlightedSlot(null)}
						>
							<img
								src={`/items/${item.imagePath}`}
								alt={item.name}
								className="item-icon"
							/>

							<span className="tooltip">
								{item.name} | {item.slotType}
							</span>

							<button
								className="sell-btn"
								onClick={(e) => {
									e.stopPropagation();
									onSell(item.id);
								}}
								title="Sprzedaj przedmiot"
							>
								💰
							</button>
						</div>
					))}

					{/* puste sloty */}
					{Array.from({
						length: Math.max(0, 5 - hero.backpack.length),
					}).map((_, i) => (
						<div key={`empty-${i}`} className="slot empty-slot" />
					))}
				</div>
			</div>
		</div>
	);
}

export default HeroPanel;
