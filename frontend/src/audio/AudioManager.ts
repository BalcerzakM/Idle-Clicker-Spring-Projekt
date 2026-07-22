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
	private effectsVolume = 0.5; // 0 – 1

	// Nowe pola dla muzyki
	private userMusicVolume = 0.5; // preferencja użytkownika (suwak)
	private musicMultiplier = 1; // mnożnik zależny od widoku (np. 1 lub 0.4)
	private musicVolume = 1; // wynikowa głośność = userMusicVolume * musicMultiplier

	constructor() {
		Object.entries(sounds).forEach(([name, src]) => {
			this.soundMap[name] = new Howl({
				src: [src],
				volume: 0.5,
			});
		});

		this.loadSettings(); // wczytuje userMusicVolume, effectsVolume, muted
		this.applyEffectsVolume(); // ustawia głośność efektów
		this.createMusicFilter();
		this.applyMusicVolume(); // ustawia początkową głośność muzyki (z wczytaną preferencją)
	}

	// --- Filtr ---
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

	// --- Efekty dźwiękowe  ---
	play(sound: SoundName) {
		this.soundMap[sound]?.play();
	}

	setEffectsVolume(value: number) {
		this.effectsVolume = Math.min(1, Math.max(0, value));
		this.applyEffectsVolume();
		this.saveSettings();
	}

	getEffectsVolume() {
		return this.effectsVolume;
	}

	private applyEffectsVolume() {
		Object.values(this.soundMap).forEach((sound) => {
			sound.volume(0.5 * this.effectsVolume);
		});
	}

	// --- Muzyka – nowe API ---

	/** Ustawia preferencję użytkownika (wywoływana z suwaka głośności muzyki) */
	setUserMusicVolume(value: number) {
		this.userMusicVolume = Math.min(1, Math.max(0, value));
		this.saveSettings();
		this.applyMusicVolume();
	}

	/** Zwraca preferencję użytkownika (do pokazania na suwaku) */
	getUserMusicVolume() {
		return this.userMusicVolume;
	}

	/** Ustawia mnożnik głośności muzyki zależny od widoku (1 – normalny, 0.4 – ściszony itp.) */
	setMusicMultiplier(multiplier: number) {
		this.musicMultiplier = Math.min(1, Math.max(0, multiplier));
		this.applyMusicVolume();
	}

	/** Aktualna (wynikowa) głośność muzyki (tylko do odczytu) */
	getMusicVolume() {
		return this.musicVolume;
	}

	/** Przelicza i stosuje wynikową głośność muzyki */
	private applyMusicVolume() {
		this.musicVolume = this.userMusicVolume * this.musicMultiplier;
		if (this.currentMusic) {
			this.currentMusic.fade(this.currentMusic.volume(), this.musicVolume, 300);
		}
	}

	// --- Odtwarzanie muzyki (zmodyfikowane) ---
	playMusic(track: MusicName) {
		if (this.currentTrack === track && this.currentMusic?.playing()) return;
		this.stopMusic();
		this.currentTrack = track;

		this.currentMusic = new Howl({
			src: [music[track]],
			loop: true,
			html5: false,
			volume: 0,
			onplay: () => this.attachMusicFilter(),
		});

		this.currentMusic.play();
		this.currentMusic.fade(0, this.musicVolume, 1000);
	}

	stopMusic() {
		if (!this.currentMusic) return;
		this.currentMusic.stop();
		this.currentMusic = undefined;
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

	// --- Wyciszenie globalne ---
	toggleMute() {
		this.muted = !this.muted;
		Howler.mute(this.muted);
		this.saveSettings();
		return this.muted;
	}

	isMuted() {
		return this.muted;
	}

	// --- localStorage ---
	private loadSettings() {
		try {
			const saved = localStorage.getItem("audioSettings");
			if (saved) {
				const { musicVolume, effectsVolume, muted } = JSON.parse(saved);
				if (typeof musicVolume === "number") this.userMusicVolume = musicVolume;
				if (typeof effectsVolume === "number")
					this.effectsVolume = effectsVolume;
				if (typeof muted === "boolean") {
					this.muted = muted;
					Howler.mute(this.muted);
				}
			}
		} catch (e) {
			console.warn("Nie udało się wczytać ustawień dźwięku", e);
		}
	}

	private saveSettings() {
		try {
			localStorage.setItem(
				"audioSettings",
				JSON.stringify({
					musicVolume: this.userMusicVolume, // zapisujemy preferencję
					effectsVolume: this.effectsVolume,
					muted: this.muted,
				}),
			);
		} catch (e) {
			console.warn("Nie udało się zapisać ustawień dźwięku", e);
		}
	}

	getMasterVolume() {
		return 1;
	}
}

export default new AudioManager();
