import { useContext } from "react";

import { AudioContext } from "../audio/AudioProvider";

export default function useAudio() {
	return useContext(AudioContext);
}
