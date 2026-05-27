import { useState, useEffect, useCallback } from "react";
import "../css/ShopView.css";

// ----- interfejsy zgodne z backendem -----
interface ItemDto {
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
	imagePath: string; // sama nazwa pliku, np. "item1.png"
}

interface ItemsAndStatsDto {
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
	equipment: Record<string, ItemDto | null>;
}

function Shop() {
	// stany sklepu
	const [items, setItems] = useState<ItemDto[]>([]);
	const [loading, setLoading] = useState<boolean>(true);
	const [error, setError] = useState<string | null>(null);
	const [buyingId, setBuyingId] = useState<number | null>(null);

	// stany bohatera
	const [hero, setHero] = useState<ItemsAndStatsDto | null>(null);

	// ---------- pobieranie danych ----------
	const fetchShopItems = useCallback(async () => {
		try {
			setLoading(true);
			setError(null);
			const res = await fetch("http://localhost:8080/api/shop");
			if (!res.ok) throw new Error("Błąd pobierania ofert");
			const data: ItemDto[] = await res.json();
			setItems(data);
		} catch (err: any) {
			setError(err.message || "Nie udało się załadować sklepu");
		} finally {
			setLoading(false);
		}
	}, []);

	const fetchCharacterData = useCallback(async () => {
		try {
			const [statsRes] = await Promise.all([
				fetch("http://localhost:8080/api/character/statsItems"),
			]);
			if (!statsRes.ok) throw new Error("Błąd pobierania danych postaci");
			const heroData: ItemsAndStatsDto = await statsRes.json();
			setHero(heroData);
		} catch (err: any) {
			console.error(err);
			setError(err.message);
		}
	}, []);

	useEffect(() => {
		fetchShopItems();
		fetchCharacterData();
	}, [fetchShopItems, fetchCharacterData]);

	// ---------- zakup ----------
	const handleBuy = async (offerId: number) => {
		try {
			setBuyingId(offerId);
			setError(null);
			const res = await fetch("http://localhost:8080/api/shop/buy", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(offerId),
			});
			if (!res.ok) {
				const errText = await res.text();
				throw new Error(errText || "Nie udało się kupić przedmiotu");
			}
			// odśwież sklep i dane postaci
			await fetchShopItems();
			await fetchCharacterData();
		} catch (err: any) {
			setError(err.message || "Wystąpił błąd podczas zakupu");
		} finally {
			setBuyingId(null);
		}
	};

	const handleRefresh = async () => {
		try {
			await fetch("http://localhost:8080/api/shop/refresh", { method: "POST" });
			await fetchShopItems();
		} catch (err: any) {
			setError("Nie udało się odświeżyć ofert");
		}
	};

	// ---------- sprzedaż przedmiotu z plecaka ----------
	const handleSell = async (backpackItemId: number) => {
		try {
			const res = await fetch("http://localhost:8080/api/shop/sell", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(backpackItemId),
			});
			if (!res.ok) throw new Error("Sprzedaż nieudana");
			await fetchCharacterData(); // odśwież plecak i pieniądze
		} catch (err: any) {
			setError(err.message || "Błąd sprzedaży");
		}
	};

	// ---------- drag & drop (ekwipunek) ----------
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

	const handleDropOnSlot = async (
		e: React.DragEvent<HTMLDivElement>,
		slotType: string,
	) => {
		e.preventDefault();
		const backpackItemId = Number(e.dataTransfer.getData("text/plain"));
		if (isNaN(backpackItemId)) return;

		// znajdź ID przedmiotu aktualnie założonego w tym slocie (może być null)
		const equippedItem = hero?.equipment?.[slotType] ?? null;
		const equipmentItemId = equippedItem?.id ?? null;

		try {
			const res = await fetch("http://localhost:8080/api/character/equip", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({
					equipmentItemId: equipmentItemId,
					backpackItemId: backpackItemId,
				}),
			});
			if (!res.ok) {
				const errText = await res.text();
				throw new Error(errText);
			}
			await fetchCharacterData(); // odśwież ekwipunek i plecak
		} catch (err: any) {
			console.error(err);
			alert(err.message || "Nie udało się założyć przedmiotu");
		}
	};

	// ---------- widok ----------
	if (loading && !hero) {
		return <div className="shop-loading">Ładowanie...</div>;
	}

	return (
		<div className="shop-hero-container">
			{/* ---------- LEWA: BOHATER ---------- */}
			<div className="hero-panel">
				{hero && (
					<>
						<div className="hero-stats">
							<span>✨ {hero.totalRizz}</span>
							<span>💪 {hero.totalStrength}</span>
							<span>🏃 {hero.totalAgility}</span>
							<span>🛡️ {hero.totalEndurance}</span>
							<span>🍀 {hero.totalLuck}</span>
						</div>

						<div className="equipment" id="equipment">
							{/* Portret */}
							<div className="portrait" id="nameSlot">
								<img
									src={`/avatars/${hero.avatarPicture}`}
									alt={hero.name}
									className="portrait-img"
								/>
								<span className="tooltip">{hero.name} |</span>
							</div>

							{/* Sloty wyposażenia */}
							{[
								"HEAD",
								"NECK",
								"UPPERBODY",
								"LOWERBODY",
								"FEET",
								"WRIST",
								"EMBLEM",
							].map((slotType) => {
								const item = hero.equipment?.[slotType] ?? null;
								return (
									<div
										key={slotType}
										className="slot equip-slot"
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

						{/* Plecak z możliwością sprzedaży */}
						<div className="inventory">
							<h3>Plecak</h3>
							<div id="inventory-grid">
								{hero.backpack.map((item) => (
									<div
										key={item.id}
										className="slot backpack-slot"
										draggable
										onDragStart={(e) => handleDragStart(e, item.id)}
									>
										<img
											src={`/items/${item.imagePath}`}
											alt={item.name}
											className="item-icon"
										/>
										<span className="tooltip">
											{item.name} | {item.slotType}
										</span>
										{/* przycisk sprzedaży (pojawia się po najechaniu) */}
										<button
											className="sell-btn"
											onClick={(e) => {
												e.stopPropagation();
												handleSell(item.id);
											}}
											title="Sprzedaj przedmiot"
										>
											💰
										</button>
									</div>
								))}
								{/* puste sloty dla zachowania siatki */}
								{Array.from({
									length: Math.max(0, 5 - hero.backpack.length),
								}).map((_, i) => (
									<div key={`empty-${i}`} className="slot empty-slot" />
								))}
							</div>
						</div>
					</>
				)}
			</div>

			{/* ---------- PRAWA: SKLEP ---------- */}
			<div className="shop-panel">
				<div className="shop-header">
					<h2>Sklep</h2>

					<button onClick={handleRefresh} className="shop-refresh-btn">
						Odśwież ofertę
					</button>
				</div>

				{error && <div className="shop-error">{error}</div>}

				{items.length === 0 ? (
					<p className="shop-empty">Brak dostępnych ofert.</p>
				) : (
					<div className="shop-grid">
						{items.map((item) => (
							<div key={item.id} className="shop-item-card">
								<div className="shop-item-image-wrapper">
									<img
										src={`/items/${item.imagePath}`}
										alt={item.name}
										className="shop-item-image"
										onError={(e) => (e.currentTarget.src = "/placeholder.png")}
									/>
								</div>
								<div className="shop-item-info">
									<h3 className="shop-item-name">{item.name}</h3>
									<p className="shop-item-desc">{item.description}</p>
									<div className="shop-item-stats">
										<span>💪 {item.totalStrength}</span>
										<span>🏃 {item.totalAgility}</span>
										<span>🛡️ {item.totalEndurance}</span>
										<span>🍀 {item.totalLuck}</span>
										<span>✨ {item.totalRizz}</span>
									</div>
									<p className="shop-item-price">💰 {item.price}</p>
									<button
										onClick={() => handleBuy(item.id)}
										disabled={buyingId === item.id}
										className="shop-buy-btn"
									>
										{buyingId === item.id ? "Kupowanie..." : "Kup"}
									</button>
								</div>
							</div>
						))}
					</div>
				)}
			</div>
		</div>
	);
}

export default Shop;
