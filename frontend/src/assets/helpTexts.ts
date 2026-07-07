export const helpTexts: Record<string, { title: string; text: string }> = {
	"/": {
		title: "Klub",
		text: "Tutaj znajduje się główne wejście do klubu.",
	},

	"/outside": {
		title: "Przed klubem",
		text: `
Możesz tutaj:

• Wejść do klubu
• Porozmawiać z bokserem
• Odwiedzić parking
• Zajrzeć do ochrony
• Sprawdzić ranking
`,
	},

	"/boxer": {
		title: "Bokser",
		text: "Tutaj rozwijasz swojego boksera.",
	},

	"/parking": {
		title: "Parking",
		text: "Tutaj znajdują się wszystkie samochody.",
	},

	"/security": {
		title: "Ochrona",
		text: "Tutaj możesz porozmawiać z ochroniarzem.",
	},

	"/ranking": {
		title: "Ranking",
		text: "Tutaj znajduje się ranking graczy.",
	},
};
