import { createContext, useContext, useState, useCallback, useEffect, useRef } from "react";
import type { ReactNode } from "react";
import {useAlert} from "./AlertContext";

interface CharacterShortInfo {
    avatarPicture: string;
    money: number;
    cristals: number;
    aura: number;
    auraLevel: number;
    nextLevelAuraRequirement: number;
    levelProgressPercent: number;
}

interface CharacterContextType {
    character: CharacterShortInfo | null;
    refreshCharacter: () => Promise<void>;
}

// 1. Tworzymy Context
const CharacterContext = createContext<CharacterContextType | undefined>(undefined);

// 2. Tworzymy Provider (Dostawcę danych)
export function CharacterProvider({ children }: { children: ReactNode }) {
    const [character, setCharacter] = useState<CharacterShortInfo | null>(null);
    const {showError, showLevelUp} = useAlert();
    const previousAuraLevelRef = useRef<number | null>(null);

    // Funkcja do pobierania/odświeżania danych portfela
    const refreshCharacter = useCallback(async () => {
        try {
            const res = await fetch("http://localhost:8080/api/character/shortInfo");
            if (res.ok) {
                const data = await res.json();
                setCharacter(data);

                if (
                    previousAuraLevelRef.current !== null &&
                    data.auraLevel > previousAuraLevelRef.current
                ) {
                    showLevelUp(`Twoja aura osiągnęła level ${data.auraLevel}!`)
                }

                previousAuraLevelRef.current = data.auraLevel;
                setCharacter(data);
            }

        } catch (err) {
            console.error("Błąd pobierania danych użytkownika:", err);
            showError("Nie udało się odświeżyć danych postaci")
        }
    }, [showError, showLevelUp]);

    // Pobieramy dane raz na starcie aplikacji
    useEffect(() => {
        refreshCharacter();
    }, [refreshCharacter]);

    return (
        <CharacterContext.Provider value={{ character, refreshCharacter }}>
            {children}
        </CharacterContext.Provider>
    );
}

// 3. Własny hook dla wygody
export function useCharacter() {
    const context = useContext(CharacterContext);
    if (!context) {
        throw new Error("useCharacter musi być użyty wewnątrz CharacterProvider");
    }
    return context;
}