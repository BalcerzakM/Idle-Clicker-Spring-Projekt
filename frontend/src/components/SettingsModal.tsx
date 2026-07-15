import { useState } from "react";
import ReportForm from "./ReportForm";
import "../css/Settings.css";

interface Props {
	onClose: () => void;
	muted: boolean;
	volume: number;
	onToggleMute: () => void;
	onVolumeChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const SettingsModal = ({
	onClose,
	muted,
	volume,
	onToggleMute,
	onVolumeChange,
}: Props) => {
	const [showReportModal, setShowReportModal] = useState(false);

	return (
		<>
			<div className="modal-overlay" onClick={onClose}>
				<div className="modal-content" onClick={(e) => e.stopPropagation()}>
					<h2>⚙️ Ustawienia</h2>

					<button onClick={onToggleMute}>
						{muted ? "Unmute 🔊" : "Mute 🔇"}
					</button>

					<div className="volume-control">
						<input
							type="range"
							min="0"
							max="1"
							step="0.01"
							value={volume}
							onChange={onVolumeChange}
						/>
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

			{showReportModal && (
				<div
					className="modal-overlay"
					onClick={() => setShowReportModal(false)}
				>
					<div className="modal-content" onClick={(e) => e.stopPropagation()}>
						<ReportForm onClose={() => setShowReportModal(false)} />
					</div>
				</div>
			)}
		</>
	);
};

export default SettingsModal;
