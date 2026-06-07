import { useNavigate } from "react-router-dom";
import "../css/Parking.css";
import CarDealer from "../assets/scenes/hover/car_dealer.png";

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
		</div>
	);
}

export default Parking;
