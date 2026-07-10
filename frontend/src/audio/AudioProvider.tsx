import { createContext } from "react";
import type { ReactNode } from "react";

import AudioManager from "./AudioManager";

export const AudioContext = createContext(AudioManager);

interface Props {
	children: ReactNode;
}

export function AudioProvider({ children }: Props) {
	return (
		<AudioContext.Provider value={AudioManager}>
			{children}
		</AudioContext.Provider>
	);
}
