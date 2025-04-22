export interface Room {
    id: string;
    settings: RoomSettings;
    players: Player[];
}

export interface RoomSettings {
    id: string;
    nbPlayerMax: number;
    gameDuration: number;
}

export interface Player {
    id: string;
    name: string;
    score: number;
    owner: boolean;
}