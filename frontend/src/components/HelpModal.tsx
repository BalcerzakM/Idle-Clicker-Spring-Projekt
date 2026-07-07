import { useLocation } from "react-router-dom";
import { helpTexts } from "../assets/helpTexts";
import { useHelp } from "../context/HelpContext";

export default function HelpModal() {
	const { pathname } = useLocation();

	const { isOpen, closeHelp } = useHelp();

	if (!isOpen) return null;

	const help = helpTexts[pathname];

	return (
		<div className="help-overlay" onClick={closeHelp}>
			<div className="help-modal" onClick={(e) => e.stopPropagation()}>
				<button className="help-close" onClick={closeHelp}>
					✖
				</button>

				<h2>{help?.title ?? "Instrukcja"}</h2>

				<p style={{ whiteSpace: "pre-line" }}>
					{help?.text ?? "Brak instrukcji dla tego widoku."}
				</p>
			</div>
		</div>
	);
}
