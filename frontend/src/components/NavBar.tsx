import PlayerAvatar from "../assets/avatars/avatar1.png";
import StandardCurrencyImg from "../assets/other/currency_standard.png";
import PremiumCurrencyImg from "../assets/other/currency_premium.png";

function NavBar() {
    
    return (
        <div className="navBar">
            <div className="navBar-character">
                <button type="button"><img src={PlayerAvatar} alt="Player avatar" className="playerAvatar"/></button>
                <div className="navBar-character-currencies">
                    <img src={StandardCurrencyImg} alt="Standard currency" className="currencyImg"/><p>1000</p>
                    <img src={PremiumCurrencyImg} alt="Premium currency" className="currencyImg"/><p>5</p>
                </div>
            </div>
            <div className="navBar-navigation">
                <button type="button">Klub</button>
                <button type="button">Szatnia</button>
                <button type="button">Palarnia</button>  
            </div>

        </div>
    )
}

export default NavBar