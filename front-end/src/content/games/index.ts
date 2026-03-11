import { cyberNexus } from './cyberNexus'
import { aetherProtocol } from './aetherProtocol'
import { voidRunners } from './voidRunners'

export const games = [cyberNexus, aetherProtocol, voidRunners]
export const gamesMap = Object.fromEntries(games.map((g) => [g.id, g]))
