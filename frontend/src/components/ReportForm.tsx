import { useState } from "react";
import "../css/ReportForm.css";
import { useAlert } from "../context/AlertContext";

interface ReportFormProps {
	onClose: () => void;
}

const ReportForm = ({ onClose }: ReportFormProps) => {
	const { showError, showInfo } = useAlert();

	const [title, setTitle] = useState("");
	const [description, setDescription] = useState("");
	const [loading, setLoading] = useState(false);

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();

		// Walidacja po stronie klienta (opcjonalna, backend też waliduje)
		if (!title.trim() || !description.trim()) {
			showError("Wypełnij wszystkie wymagane pola.");
			return;
		}

		setLoading(true);
		try {
			const response = await fetch("http://localhost:8080/api/report", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				credentials: "include", // jeśli używasz ciasteczek sesyjnych
				body: JSON.stringify({
					title: title.trim(),
					description: description.trim(),
				}),
			});

			if (!response.ok) {
				const errorText = await response.text();
				throw new Error(errorText || "Nie udało się wysłać zgłoszenia");
			}

			showInfo("Zgłoszenie zostało wysłane!");
			setTitle("");
			setDescription("");
			onClose();
		} catch (err: any) {
			console.error(err);
			showError(err.message || "Błąd połączenia z serwerem");
		} finally {
			setLoading(false);
		}
	};

	return (
		<div className="report-form-container">
			<div className="report-form-header">
				<h2 className="report-form-title">📜 ZGŁOŚ USTERKĘ</h2>
				<button className="close-button" onClick={onClose} aria-label="Zamknij">
					✖
				</button>
			</div>
			<form onSubmit={handleSubmit} className="report-form">
				<input
					type="text"
					placeholder="Tytuł zgłoszenia"
					value={title}
					onChange={(e) => setTitle(e.target.value)}
					className="report-input"
					required
				/>

				<textarea
					placeholder="Opisz szczegółowo swój problem..."
					value={description}
					onChange={(e) => setDescription(e.target.value)}
					className="report-textarea"
					required
				/>

				<button type="submit" disabled={loading} className="report-submit-btn">
					{loading ? "Wysyłanie..." : "Wyślij zgłoszenie"}
				</button>
			</form>
		</div>
	);
};

export default ReportForm;
