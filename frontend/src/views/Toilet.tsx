import { useNavigate } from "react-router-dom";
import "../css/ToiletView.css";
import premiumHover from "../assets/scenes/hover/premium_hover.png";

function Toilet() {
    const navigate = useNavigate();

    return (
        <div className="toilet">
            <div className="toilet-premium">
                <img
                    src={premiumHover}
                    alt="premium_hover"
                    width={200}
                    height={308}
                    className="hover-image"
                    onClick={() => navigate("/premium")}
                />
            </div>
            <p className="toilet-premiumTextBox">Podejrzany typ</p>
        </div>
    );
}

export default Toilet;