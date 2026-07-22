import { useEffect, useRef } from "react";
import { Routes, Route, useLocation } from "react-router-dom";
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
import Boss from "./views/Boss";
import DrinkShop from "./views/DrinkShop";
import GangList from "./views/GangList";
import MyGang from "./views/MyGang";
import { HelpProvider } from "./context/HelpContext";
import HelpModal from "./components/HelpModal";
import { AudioProvider } from "./audio/AudioProvider";
import useAudio from "./audio/useAudio";

function AppContent() {
	const gameRef = useRef<HTMLDivElement | null>(null);
	const audio = useAudio();
	const location = useLocation();

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

	useEffect(() => {
		audio.playMusic("menu");
	}, []);

	useEffect(() => {
		const cleanMusicViews = ["/", "/barman", "/shop", "/drink-shop"];
		const clean = cleanMusicViews.includes(location.pathname);

		audio.setMusicMultiplier(clean ? 1 : 0.4);
		audio.setMusicMuffled(!clean, 700);
	}, [location.pathname, audio]);

	return (
		<HelpProvider>
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
							<Route path="/boss" element={<Boss />} />
							<Route path="/drink-shop" element={<DrinkShop />} />
							<Route path="/gang-list" element={<GangList />} />
							<Route path="/my-gang" element={<MyGang />} />
						</Routes>
						<HelpModal />
					</div>
				</div>
			</div>
		</HelpProvider>
	);
}

export default function App() {
	return (
		<AudioProvider>
			<AppContent />
		</AudioProvider>
	);
}
