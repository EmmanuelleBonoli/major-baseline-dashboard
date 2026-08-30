import { spaceMatch } from './spaceMatch.ts'
import { wordRiders } from './wordRiders'

export const games = [spaceMatch, wordRiders]
export const gamesMap = Object.fromEntries(games.map((g) => [g.id, g]))
