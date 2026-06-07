export interface ItemDto {
    id: number;
    itemName: string;
    itemDescription: string;
    slotType: string;
    itemType: string;
    totalRizz: number;
    totalStrength: number;
    totalAgility: number;
    totalEndurance: number;
    totalLuck: number;
    price: number;
    imagePath: string;
}

export function itemTooltip(item: ItemDto) {
    return (
        <>
            <div className="tooltip-name">
                {item.itemName}
            </div>

            <div className="tooltip-type">
                {item.itemType}
            </div>

            {item.slotType !== "NONE" && (
                <div className="tooltip-slot">
                    {item.slotType}
                </div>
            )}

            {item.itemDescription !== "NONE" && (
                <div className="tooltip-description">
                    {item.itemDescription}
                </div>
            )}

            {item.totalStrength !== 0 && (
                <div className="tooltip-stat">
                    💪 {item.totalStrength}
                </div>
            )}

            {item.totalAgility !== 0 && (
                <div className="tooltip-stat">
                    🏃 {item.totalAgility}
                </div>
            )}

            {item.totalEndurance !== 0 && (
                <div className="tooltip-stat">
                    🛡️ {item.totalEndurance}
                </div>
            )}

            {item.totalLuck !== 0 && (
                <div className="tooltip-stat">
                    🍀 {item.totalLuck}
                </div>
            )}

            {item.totalRizz !== 0 && (
                <div className="tooltip-stat">
                    ✨ {item.totalRizz}
                </div>
            )}
        </>
    );
}