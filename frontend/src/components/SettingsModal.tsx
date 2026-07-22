import { useState } from "react";
import ReportForm from "./ReportForm";
import "../css/Settings.css";

interface Props {
	onClose: () => void;
	muted: boolean;
	musicVolume: number;
	effectsVolume: number;
	onToggleMute: () => void;
	onMusicVolumeChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
	onEffectsVolumeChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const SettingsModal = ({
	onClose,
	muted,
	musicVolume,
	effectsVolume,
	onToggleMute,
	onMusicVolumeChange,
	onEffectsVolumeChange,
}: Props) => {
	const [showReportModal, setShowReportModal] = useState(false);

	const musicPercent = Math.round(musicVolume * 100);
	const effectsPercent = Math.round(effectsVolume * 100);

	return (
		<>
			{/* Overlay ustawień */}
			<div className="modal-overlay" onClick={onClose}>
				<div className="modal-content" onClick={(e) => e.stopPropagation()}>
					<div className="modal-header">
						<h2>⚙️ Ustawienia</h2>
						<button
							className="modal-close-icon"
							onClick={onClose}
							aria-label="Zamknij"
						>
							✕
						</button>
					</div>

					<button onClick={onToggleMute}>
						{muted ? "Odcisz 🔊" : "Wycisz 🔇"}
					</button>

					{/* Suwak głośności muzyki */}
					<div className="volume-control-wrapper">
						<div className="volume-label">
							Głośność Muzyki <span>{musicPercent}%</span>
						</div>
						<input
							type="range"
							min="0"
							max="1"
							step="0.01"
							value={musicVolume}
							onChange={onMusicVolumeChange}
							className="volume-slider"
						/>
						<div className="volume-limits">
							<span>0%</span>
							<span>100%</span>
						</div>
					</div>

					{/* Suwak głośności efektów */}
					<div className="volume-control-wrapper">
						<div className="volume-label">
							Głośność Efektów <span>{effectsPercent}%</span>
						</div>
						<input
							type="range"
							min="0"
							max="1"
							step="0.01"
							value={effectsVolume}
							onChange={onEffectsVolumeChange}
							className="volume-slider"
						/>
						<div className="volume-limits">
							<span>0%</span>
							<span>100%</span>
						</div>
					</div>

					<button
						className="report-btn"
						onClick={() => setShowReportModal(true)}
					>
						⚠️ Zgłoś usterkę
					</button>

					<button className="modal-close-btn" onClick={onClose}>
						Zamknij
					</button>
				</div>
			</div>

			{/* Overlay formularza zgłoszenia */}
			{showReportModal && (
				<div
					className="modal-overlay modal-overlay--report"
					onClick={() => setShowReportModal(false)}
				>
					<div
						className="modal-content modal-content--report"
						onClick={(e) => e.stopPropagation()}
					>
						<ReportForm onClose={() => setShowReportModal(false)} />
					</div>
				</div>
			)}
		</>
	);
};

export default SettingsModal;
