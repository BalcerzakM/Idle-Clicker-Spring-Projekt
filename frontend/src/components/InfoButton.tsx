import { useHelp } from "../context/HelpContext";
import { createAudioHandlers } from "../utils/AudioHelpers";
import useAudio from "../audio/useAudio";

export default function InfoButton() {
	const { openHelp } = useHelp();
	const audio = useAudio();
	const { playClick } = createAudioHandlers(audio);
	return (
		<button
			className="info-button"
			onClick={() => {
				openHelp();
				playClick();
			}}
		>
			ℹ️
		</button>
	);
}
