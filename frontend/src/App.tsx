import { useState, useEffect, useRef } from "react";
import NavBar from "./components/NavBar";
import ClubMain from "./views/ClubMain";
// import "./App.css";

function App() {
	const [count, setCount] = useState(0);
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
					<ClubMain />
				</div>
			</div>
		</div>
	);
}

export default App;
