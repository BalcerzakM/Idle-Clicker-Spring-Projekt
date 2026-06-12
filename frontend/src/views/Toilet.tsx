import { useNavigate } from "react-router-dom";
import "../css/ToiletView.css";
import premiumHover from "../assets/scenes/hover/premium_hover.png";
import leaveToiletHover from "../assets/scenes/hover/outside_hover.png";

function Toilet() {
    const navigate = useNavigate();

    return (
        <div className="toilet">
            <div className="toilet-premium">
                <img
                    src={premiumHover}
                    alt="premium_hover"
                    width={207}
                    height={313}
                    className="hover-image"
                    onClick={() => navigate("/premium")}
                />
            </div>
            <p className="toilet-premiumTextBox">Podejrzany typ</p>

            <div className="toilet-leaveToilet">
                <img
                    src={leaveToiletHover}
                    alt="leave_toilet_hover"
                    width={725}
                    height={204}
                    className="hover-image"
                    onClick={() => navigate("/")}
                />
            </div>
            <p className="toilet-leaveToiletTextBox">Klub</p>
        </div>
    );
}

export default Toilet;