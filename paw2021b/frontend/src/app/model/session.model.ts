export class Session {
  constructor(
    private _token: string,
    private _tokenExpirationDate: Date,
    private _refreshToken: String,
  ) {}

  get token() {
    if (!this._tokenExpirationDate || new Date() > this._tokenExpirationDate) {
      return this._refreshToken;
    }
    return this._token;
  }
}
