import { astroPuyo } from './astroPuyo'
import { wordRiders } from './wordRiders'

export const games = [astroPuyo, wordRiders]
export const gamesMap = Object.fromEntries(games.map((g) => [g.id, g]))
