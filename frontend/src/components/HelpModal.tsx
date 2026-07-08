import { useLocation } from "react-router-dom";
import { helpTexts } from "../assets/helpTexts";
import { useHelp } from "../context/HelpContext";
import LogoImg from "../assets/other/logo.png";

export default function HelpModal() {
	const { pathname } = useLocation();

	const { isOpen, closeHelp } = useHelp();

	if (!isOpen) return null;

	const help = helpTexts[pathname];

	return (
		<div className="help-overlay" onClick={closeHelp}>
			<div className="help-modal" onClick={(e) => e.stopPropagation()}>
                <div className="help-content">
                    <img
                        src={LogoImg}
                        alt="Logo"
                        className="help-logo"
                    />
                    <button className="help-close" onClick={closeHelp}>
                        ✖
                    </button>
                    <div className="help-text-wrapper">
                        <h2>{help?.title ?? "Instrukcja"}</h2>
                        <hr/>

                        <p style={{ whiteSpace: "pre-line" }}>
                            {help?.text ?? "Brak instrukcji dla tego widoku."}
                        </p>
                    </div>
                </div>
			</div>
		</div>
	);
}
