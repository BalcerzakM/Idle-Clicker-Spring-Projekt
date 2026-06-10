import { useEffect, useRef } from "react";
import { Routes, Route } from "react-router-dom";
import NavBar from "./components/NavBar";
import ClubMain from "./views/ClubMain";
import Shop from "./views/Shop";
import Barman from "./views/Barman";
import Player from "./views/Player";
import Outside from "./views/Outside";
import Boxer from "./views/Boxer";
import Parking from "./views/Parking";
import Security from "./views/Security";
import Toilet from "./views/Toilet";
import Premium from "./views/Premium";
import CarDealer from "./views/CarDealer";
import RankingView from "./views/RankingView";

function App() {
	const gameRef = useRef<HTMLDivElement | null>(null); // referencja do kontenera #root

	useEffect(() => {
		const gameElement = gameRef.current;
		if (!gameElement) return;

		const scaleGame = () => {
			const scaleX = window.innerWidth / 1700;
			const scaleY = window.innerHeight / 850;
			const scale = Math.min(scaleX, scaleY);
			gameElement.style.transform = `scale(${scale})`;
		};

		scaleGame();
		window.addEventListener("resize", scaleGame);
		return () => window.removeEventListener("resize", scaleGame);
	}, []);

	return (
		<div className="gameWrapper">
			<div id="root" className="app-root">
				<div ref={gameRef} className="game-content">
					<NavBar />
					<Routes>
						<Route index element={<ClubMain />} />
						<Route path="/shop" element={<Shop />} />
						<Route path="/barman" element={<Barman />} />
						<Route path="/player" element={<Player />} />
						<Route path="/outside" element={<Outside />} />
						<Route path="/boxer" element={<Boxer />} />
						<Route path="/parking" element={<Parking />} />
						<Route path="/security" element={<Security />} />
						<Route path="/toilet" element={<Toilet />} />
						<Route path="/premium" element={<Premium />} />
						<Route path="/car-dealer" element={<CarDealer />} />
						<Route path="/ranking" element={<RankingView />} />
					</Routes>
				</div>
			</div>
		</div>
	);
}

export default App;
