export interface Room {
    id: string;
    settings: RoomSettings;
    players: Player[];
    playing: boolean;
}

export interface RoomSettings {
    id: string;
    nbPlayerMax: number;
    gameDuration: number;
    genres: MusicGenre[];
}

export interface Player {
    id: string;
    name: string;
    score: number;
    owner: boolean;
}

export interface MusicGenre {
    id: number;
    name: string;
}

export interface Music {
    id: string;
    title: string;
    titleShort: string;
    preview: string;
    artist: string;
    played: boolean
}