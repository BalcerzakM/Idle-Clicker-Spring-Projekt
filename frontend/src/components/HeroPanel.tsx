import "../css/HeroPanel.css";

export interface ItemDto {
	id: number;
	itemName: string;
	itemDescription: string;
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
	onEquip: (backpackItemId: number) => void;
	highlightedSlot: string | null;
	onHoverSlot: (slotType: string | null) => void;
}

function HeroPanel({
	hero,
	onEquip,
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
		onEquip(backpackItemId);
	};

	const itemTooltip = (item: ItemDto) =>
		`${item.itemName} | ${item.slotType}\n💪 ${item.totalStrength} \n🏃 ${item.totalAgility} \n🛡️ ${item.totalEndurance}\n🍀 ${item.totalLuck} \n✨ ${item.totalRizz}`;

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
							className={`slot equip-slot ${isHighlighted ? "highlight-slot" : ""}`}
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
					{hero.backpack.map((item) => (
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
		</div>
	);
}

export default HeroPanel;
