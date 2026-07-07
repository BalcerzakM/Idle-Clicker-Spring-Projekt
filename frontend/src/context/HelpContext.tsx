import { createContext, useContext, useState } from "react";
import type { ReactNode } from "react";

interface HelpContextType {
	isOpen: boolean;
	openHelp: () => void;
	closeHelp: () => void;
}

const HelpContext = createContext<HelpContextType | null>(null);

export function HelpProvider({ children }: { children: ReactNode }) {
	const [isOpen, setIsOpen] = useState(false);

	return (
		<HelpContext.Provider
			value={{
				isOpen,
				openHelp: () => setIsOpen(true),
				closeHelp: () => setIsOpen(false),
			}}
		>
			{children}
		</HelpContext.Provider>
	);
}

export function useHelp() {
	const context = useContext(HelpContext);

	if (!context) {
		throw new Error("useHelp musi być użyty wewnątrz HelpProvider");
	}

	return context;
}
