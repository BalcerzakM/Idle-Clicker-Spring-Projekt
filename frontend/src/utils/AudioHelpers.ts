export const createAudioHandlers = (audio: any) => {
	return {
		playClick: () => audio.play("click"),
		playHover: () => audio.play("hover"),
		navigateWithClick: (navigate: any, path: string) => {
			audio.play("click");
			navigate(path);
		},
	};
};
