import "../css/BossView.css";
import questCard from "../assets/scenes/other/special_quest_card.png";

function Boss() {

    return (
        <div className="boss">
            <div className="boss-card">
                <img
                    src={questCard}
                    alt="boss_card"
                    width={735}
                    height={400}
                    className="boss-card-img"
                />

            </div>
        </div>
    );
}

export default Boss;