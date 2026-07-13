import { Howl, Howler } from "howler";

import { sounds, music } from "./assets";
import type { SoundName, MusicName } from "./audio.types";

class AudioManager {
	private soundMap: Record<string, Howl> = {};

	private currentMusic?: Howl;
	private currentTrack?: MusicName;

	private musicFilter?: BiquadFilterNode;
	private musicSourceGain?: GainNode;

	private muted = false;
	private masterVolume = 1;

	constructor() {
		Object.entries(sounds).forEach(([name, src]) => {
			this.soundMap[name] = new Howl({
				src: [src],
				volume: 0.5,
			});
		});

		this.createMusicFilter();
	}

	private createMusicFilter() {
		const ctx = Howler.ctx;

		if (!ctx || this.musicFilter) return;

		this.musicFilter = ctx.createBiquadFilter();

		this.musicFilter.type = "lowpass";
		this.musicFilter.frequency.value = 20000;

		this.musicSourceGain = ctx.createGain();

		this.musicSourceGain.gain.value = 1;

		this.musicSourceGain.connect(this.musicFilter).connect(Howler.masterGain);
	}

	private attachMusicFilter() {
		if (!this.currentMusic) return;

		const node = (this.currentMusic as any)._sounds?.[0]?._node;

		if (!node || !this.musicSourceGain) {
			console.log("NO MUSIC NODE");
			return;
		}

		try {
			node.disconnect();
			node.connect(this.musicSourceGain);

			console.log("FILTER CONNECTED");
		} catch (e) {
			console.log(e);
		}
	}

	play(sound: SoundName) {
		this.soundMap[sound]?.play();
	}

	playMusic(track: MusicName) {
		if (this.currentTrack === track && this.currentMusic?.playing()) {
			return;
		}

		this.stopMusic();

		this.currentTrack = track;

		this.currentMusic = new Howl({
			src: [music[track]],

			loop: true,

			html5: false,

			volume: 0,

			onplay: () => {
				this.attachMusicFilter();
			},
		});

		this.currentMusic.play();

		this.currentMusic.fade(0, 1, 1000);
	}

	stopMusic() {
		if (!this.currentMusic) return;

		this.currentMusic.stop();
		this.currentMusic = undefined;
	}

	setMusicVolume(volume: number, duration = 500) {
		if (!this.currentMusic) return;

		this.currentMusic.fade(this.currentMusic.volume(), volume, duration);
	}

	setMusicMuffled(enabled: boolean, duration = 500) {
		if (!this.musicFilter) {
			console.log("NO FILTER");
			return;
		}

		const ctx = Howler.ctx;

		const frequency = enabled ? 800 : 20000;

		this.musicFilter.frequency.cancelScheduledValues(ctx.currentTime);

		this.musicFilter.frequency.linearRampToValueAtTime(
			frequency,
			ctx.currentTime + duration / 1000,
		);
	}

	setMasterVolume(value: number) {
		this.masterVolume = value;

		if (!this.muted) {
			Howler.volume(value);
		}
	}

	getMasterVolume() {
		return this.masterVolume;
	}

	toggleMute() {
		this.muted = !this.muted;

		Howler.mute(this.muted);

		return this.muted;
	}

	isMuted() {
		return this.muted;
	}
}

export default new AudioManager();
