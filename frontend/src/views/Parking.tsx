import { useNavigate } from "react-router-dom";
import "../css/Parking.css";
import CarDealer from "../assets/scenes/hover/car_dealer.png";
import leaveParkingHover from "../assets/scenes/hover/outside_hover.png";

function Parking() {
	const navigate = useNavigate();

	return (
		<div className="parking">
			<div className="parking-carDealer">
				<img
					src={CarDealer}
					alt="carDealer-hover"
					className="hover-image"
					onClick={() => navigate("/car-dealer")}
				/>
			</div>
            <p className="parking-carDealerTextBox">Dealer Kluczyków</p>

            <div className="parking-leaveParking">
                <img
                    src={leaveParkingHover}
                    alt="parking-leave-hover"
                    className="hover-image"
                    onClick={() => navigate("/outside")}
                />
            </div>
			<p className="parking-leaveParkingTextBox">Palarnia</p>
		</div>
	);
}

export default Parking;
