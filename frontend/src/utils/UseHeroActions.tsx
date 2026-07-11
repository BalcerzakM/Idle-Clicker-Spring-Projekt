import { useState, useCallback} from "react";
import { useAlert } from "../context/AlertContext";
import { useCharacter} from "../context/CharacterContext";
import type { ItemsAndStatsDto} from "../components/HeroPanel";

export function useHeroActions() {
    const { showError } = useAlert();
    const { refreshCharacter } = useCharacter();

    const [hero, setHero] = useState<ItemsAndStatsDto | null>(null);
    const [highlightedSlot, setHighlightedSlot] = useState<string | null>(null);

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


    const handleEquip = async (backpackItemId: number) => {
        if (!hero) return;

        const backpackItem = hero.backpack.find((i) => i.id === backpackItemId);

        if (!backpackItem) return;

        const equippedItem = hero.equipment.find((e) => e.slotType === backpackItem.slotType);

        try {
            const res = await fetch("http://localhost:8080/api/character/equip", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    equipmentItemId: equippedItem?.id ?? null,
                    backpackItemId
                }),
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

    const handleDrink = async (backpackItemId: number) => {
        try {
            const res = await fetch("http://localhost:8080/api/character/drink", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(backpackItemId),
            });

            if (!res.ok) {
                const error = await res.json();
                showError(error.message);
                return;
            }

            await fetchCharacterData();
            await refreshCharacter();
        } catch (err: any) {
            console.error(err);
            showError("Brak połączenia z serwerem");
        }
    }

    const handleUseItem = async (backpackItemId: number) => {
        if(!hero) return;

        const item = hero.backpack.find(i => i.id === backpackItemId);

        if (!item) return;

        switch (item.itemType) {
            case "EQUIPMENT":
                await handleEquip(backpackItemId);
                break;
            case "DRINK":
                await handleDrink(backpackItemId);
                break;
        }
    }

    // Obsługa podświetlania slotu
    const handleHoverSlot = (slotType: string | null) => {
        setHighlightedSlot(slotType);
    };

    return {
        hero,
        fetchCharacterData,
        handleUseItem,
        highlightedSlot,
        handleHoverSlot,
        setHighlightedSlot
    };

}