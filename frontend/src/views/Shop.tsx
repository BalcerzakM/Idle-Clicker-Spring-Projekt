import { useState, useEffect, useCallback } from "react";
import { useCharacter } from "../context/CharacterContext";
import HeroPanel from "../components/HeroPanel";
import "../css/ShopView.css";
import type { ItemDto, ItemsAndStatsDto } from "../components/HeroPanel";
import PremiumCurrencyImg from "../assets/other/currency_premium.png";
import { useAlert } from "../context/AlertContext.tsx";

function Shop() {
	const { showError } = useAlert();
	const [items, setItems] = useState<ItemDto[]>([]);
	const [loading, setLoading] = useState<boolean>(true);
	const [buyingId, setBuyingId] = useState<number | null>(null);
	const [hero, setHero] = useState<ItemsAndStatsDto | null>(null);
	const { refreshCharacter } = useCharacter();
	const [highlightedSlot, setHighlightedSlot] = useState<string | null>(null);

	// ---------- pobieranie danych ----------
	const fetchShopItems = useCallback(async () => {
		try {
			setLoading(true);
			const res = await fetch("http://localhost:8080/api/shop");
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Nie udało się wczytać ofert");
				return;
			}
			const data: ItemDto[] = await res.json();
			setItems(data);
		} catch (err: any) {
			console.error(err);
			showError("Brak połączenia z serwerem");
		} finally {
			setLoading(false);
		}
	}, [showError]);

	const fetchCharacterData = useCallback(async () => {
		try {
			const res = await fetch("http://localhost:8080/api/character/statsItems");
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Nie udało się wczytać danych postaci");
				return;
			}
			const heroData: ItemsAndStatsDto = await res.json();
			setHero(heroData);
		} catch (err: any) {
			console.error(err);
			showError("Brak połączenia z serwerem");
		}
	}, [showError]);

	useEffect(() => {
		fetchShopItems();
		fetchCharacterData();
	}, [fetchShopItems, fetchCharacterData]);

	// ---------- zakup ----------
	const handleBuy = async (offerId: number) => {
		try {
			setBuyingId(offerId);
			const res = await fetch("http://localhost:8080/api/shop/buy", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(offerId),
			});
			if (!res.ok) {
				const error = await res.json();
				showError(error.message);
				return;
			}
			await fetchShopItems();
			await fetchCharacterData();
			await refreshCharacter();
		} catch (err: any) {
			console.error(err);
			showError("Brak połączenia z serwerem");
		} finally {
			setBuyingId(null);
		}
	};

	const handleRefresh = async () => {
		try {
			const res = await fetch("http://localhost:8080/api/shop/refresh", {
				method: "POST",
			});
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Nie udało się odświeżyć ofert");
				return;
			}
			await fetchShopItems();
			await refreshCharacter();
		} catch (err: any) {
			console.error(err);
			showError("Brak połączenia z serwerem");
		}
	};

	// ---------- sprzedaż ----------
	const handleSell = async (backpackItemId: number) => {
		try {
			const res = await fetch("http://localhost:8080/api/shop/sell", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(backpackItemId),
			});
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Nie udało się sprzedać przedmiotu");
				return;
			}
			await fetchCharacterData();
			await refreshCharacter();
		} catch (err: any) {
			console.error(err);
			showError("Brak połączenia z serwerem");
		}
	};

	// ---------- equip ----------
	const handleEquip = async (backpackItemId: number) => {
		if (!hero) return;
		const backpackItem = hero.backpack.find((i) => i.id === backpackItemId);
		if (!backpackItem) return;
		const slotType = backpackItem.slotType;
		const equippedItem =
			hero.equipment.find((e) => e.slotType === slotType) ?? null;
		const equipmentItemId = equippedItem?.id ?? null;

		try {
			const res = await fetch("http://localhost:8080/api/character/equip", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ equipmentItemId, backpackItemId }),
			});
			if (!res.ok) {
				const error = await res.json();
				showError(error.message || "Nie udało się założyć przedmiotu");
				return;
			}
			await fetchCharacterData();
		} catch (err: any) {
			console.error(err);
			showError("Brak połączenia z serwerem");
		}
	};

	// Obsługa podświetlania slotu
	const handleHoverSlot = (slotType: string | null) => {
		setHighlightedSlot(slotType);
	};

	// ---------- Drag & drop sprzedaż ----------
	const handleDragOverShop = (e: React.DragEvent<HTMLDivElement>) => {
		// Zezwalamy tylko jeśli przeciągany jest przedmiot (ma ustawiony text/plain)
		if (e.dataTransfer.types.includes("text/plain")) {
			e.preventDefault();
			e.dataTransfer.dropEffect = "move";
		}
	};

	const handleDropOnShop = (e: React.DragEvent<HTMLDivElement>) => {
		e.preventDefault();
		const backpackItemId = Number(e.dataTransfer.getData("text/plain"));
		if (!isNaN(backpackItemId)) {
			handleSell(backpackItemId);
		}
	};

	if (loading && !hero) {
		return <div className="shop-loading">Ładowanie...</div>;
	}

	return (
		<div className="shop-hero-container">
			{hero && (
				<HeroPanel
					hero={hero}
					onSell={handleSell}
					onEquip={handleEquip}
					highlightedSlot={highlightedSlot}
					onHoverSlot={handleHoverSlot}
				/>
			)}

			<div
				className="shop-panel"
				onDragOver={handleDragOverShop}
				onDrop={handleDropOnShop}
			>
				<div className="shop-header">
					<button onClick={handleRefresh} className="shop-refresh-btn">
						Odśwież ofertę za 1
						<img
							src={PremiumCurrencyImg}
							alt="Premium currency"
							className="currencyImg"
							width={15}
							height={15}
						/>
					</button>
				</div>

				{items.length === 0 ? (
					<p className="shop-empty">Brak dostępnych ofert.</p>
				) : (
					<div className="shop-grid">
						{items.map((item) => (
							<div
								key={item.id}
								className="shop-item-card"
								onMouseEnter={() => setHighlightedSlot(item.slotType)}
								onMouseLeave={() => setHighlightedSlot(null)}
							>
								<div className="shop-item-image-wrapper">
									<img
										src={`/items/${item.imagePath}`}
										alt={item.itemName}
										className="shop-item-image"
										onError={(e) => (e.currentTarget.src = "/placeholder.png")}
									/>
									<div className="tooltip">
										{item.itemName} | {item.slotType}
										<br />
										💪 {item.totalStrength}
										<br />
										🏃 {item.totalAgility}
										<br />
										🛡️ {item.totalEndurance}
										<br />
										🍀 {item.totalLuck}
										<br />✨ {item.totalRizz}
									</div>
								</div>
								<div className="shop-item-info">
									<h3 className="shop-item-name">{item.itemName}</h3>
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
