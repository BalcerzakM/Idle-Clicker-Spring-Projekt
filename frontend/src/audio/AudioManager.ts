import { Howl, Howler } from "howler";

import { sounds, music } from "./assets";

import type { SoundName, MusicName } from "./audio.types";

class AudioManager {
	private soundMap: Record<string, Howl> = {};

	private currentMusic?: Howl;

	constructor() {
		Object.entries(sounds).forEach(([name, src]) => {
			this.soundMap[name] = new Howl({
				src: [src],

				volume: 0.5,

				preload: true,
			});
		});
	}

	play(sound: SoundName) {
		const audio = this.soundMap[sound];

		if (!audio) return;

		audio.play();
	}

	playMusic(track: MusicName) {
		if (this.currentMusic) {
			this.currentMusic.fade(this.currentMusic.volume(), 0, 500);

			this.currentMusic.stop();
		}

		this.currentMusic = new Howl({
			src: [music[track]],

			loop: true,

			volume: 0,
		});

		this.currentMusic.play();

		this.currentMusic.fade(0, 0.4, 1000);
	}

	stopMusic() {
		if (!this.currentMusic) return;

		this.currentMusic.fade(this.currentMusic.volume(), 0, 500);

		setTimeout(() => {
			this.currentMusic?.stop();
		}, 500);
	}

	setMasterVolume(value: number) {
		Howler.volume(value);
	}

	mute(value: boolean) {
		Howler.mute(value);
	}
}

export default new AudioManager();
