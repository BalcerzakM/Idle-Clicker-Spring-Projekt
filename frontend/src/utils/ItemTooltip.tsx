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

export function itemTooltip(item: ItemDto): string {
    return [
        item.itemName,
        item.itemType,
        item.slotType !== "NONE" ? item.slotType : null,
        item.itemDescription !== "NONE" ? item.itemDescription : null,

        item.totalStrength !== 0 ? `💪 ${item.totalStrength}` : null,
        item.totalAgility !== 0 ? `🏃 ${item.totalAgility}` : null,
        item.totalEndurance !== 0 ? `🛡️ ${item.totalEndurance}` : null,
        item.totalLuck !== 0 ? `🍀 ${item.totalLuck}` : null,
        item.totalRizz !== 0 ? `✨ ${item.totalRizz}` : null,
    ]
        .filter(Boolean)
        .join("\n");
}