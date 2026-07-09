import { useLocation } from "react-router-dom";
import { helpTexts } from "../assets/helpTexts";
import { useHelp } from "../context/HelpContext";
import LogoImg from "../assets/other/logo.png";
import {useState} from "react";

export default function HelpModal() {
	const { pathname } = useLocation();
	const { isOpen, closeHelp } = useHelp();
    const [isClosing, setIsClosing] = useState(false);

    const handleClose = () => {
        setIsClosing(true);

        setTimeout(() => {
            setIsClosing(false);
            closeHelp();
        }, 200);
    }

	if (!isOpen) return null;

	const help = helpTexts[pathname];

	return (
		<div className={`help-overlay ${isClosing ? "help-closing" : ""}`} onClick={handleClose}>
			<div className="help-modal" onClick={(e) => e.stopPropagation()}>
                <div className="help-content">
                    <img
                        src={LogoImg}
                        alt="Logo"
                        className="help-logo"
                    />
                    <button className="help-close" onClick={handleClose}>
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
