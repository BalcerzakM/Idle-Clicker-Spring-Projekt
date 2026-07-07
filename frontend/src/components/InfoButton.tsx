import { useHelp } from "../context/HelpContext";

export default function InfoButton() {
	const { openHelp } = useHelp();

	return (
		<button className="info-button" onClick={openHelp}>
			ℹ️
		</button>
	);
}
