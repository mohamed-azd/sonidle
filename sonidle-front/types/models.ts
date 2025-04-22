export interface Room {
    id: String;
    settings: RoomSettings;
    players: Player[];
}

export interface RoomSettings {
    id: String;
    nbPlayerMax: number;
    gameDuration: number;
}

export interface Player {
    id: String;
    name: string;
    score: number;
    owner: boolean;
}