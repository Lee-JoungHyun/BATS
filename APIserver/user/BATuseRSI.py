import pyupbit
import time

info = ""


class Auto:
    def __init__(self):
        # API 연결
        # f = open("C:/Users/ask57/Desktop/Bats/업비트.txt")
        # lines = f.readlines()
        # access = lines[1].strip()   # access key
        # secret = lines[3].strip()   # secret key
        # f.close()
        access = "9p0bCmGItR0WxJEcPyxGOZpxmSvMQnigkeH48zxS"
        secret = "OxnPsR5vLxLu8sxxPsIDIFfrYzLkEM4RHNyRxLwF"
        self.size = 200
        self.upbit = pyupbit.Upbit(access, secret)

    def main(self, coin="KRW-ETH", auto=1, spend=10000, interval=1):
        global info
        self.coin = coin
        self.spend = spend
        self.buyPrice = 0

        # interval 처리
        if interval == 10080:
            timeRange = "week"
        elif interval == 1440:
            timeRange = "day"
        else:
            timeRange = f"minute{interval}"

        # 현재 코인 잔액 표시, 출력
        krwBal = self.upbit.get_balance("KRW")
        coinBal = self.upbit.get_balances(self.coin)
        coinPrice = pyupbit.get_current_price(self.coin)
        # coinx = round(coinPrice * coinBal, 0)
        # print(coinx)
        print(f"원화 잔고: {int(round(krwBal, 0))}원")
        # print(f"{self.coin} 잔고: {coinx}원\n")

        self.buy = False
        while 1:
            # 5분봉 200개 불러오기
            self.df = pyupbit.get_ohlcv(ticker=self.coin, interval=timeRange, count=self.size)
            print("\n\n===========데이터을 입력했습니다==============")
            print(self.df[-5:])
            info = info + "\n\n===========데이터을 입력했습니다==============" + str(self.df[-5:])
            # 매수, 매도 결정
            self.count = 0
            self.RSI()
            print("\n")
            info = info + "\n"
            self.trade()
            print("\n")
            info = info + "\n"

            if not auto:
                break
            print(f"{interval * 30}초 후에 다시 시작합니다")
            info = info + str(interval * 30) + "초 후에 다시 시작합니다"
            time.sleep(interval * 30)

    def RSI(self):
        global info
        n = 14  # 사용할 데이터 수
        up, down = [0, 0], [0, 0]  # 기준이 될 상승량, 하락량

        # rsi 값 계산
        for i in range(1, n + 1):
            var = self.df["close"][-i] - self.df["close"][-i - 1]
            if var > 0:
                up[0] += var
                up[1] += 1
            else:
                down[0] += -var
                down[1] += 1
        au = up[0] / up[1]
        ad = down[0] / down[1]
        rsi = (au / (au + ad)) * 100

        # rsi 값 출력, 매수매도 결정
        print(f"RSI: {round(rsi, 1)}%")
        info = info + "RSI: " + str(round(rsi, 1)) + "%"
        print("RSI:", end=" ")
        info = info + "RSI: "

        if rsi > 70 and self.df["close"][-1] < max(self.df["close"][-n:]) and self.buy == True:
            print("매도합니다")
            info = info + "매도합니다."
            self.count += 1
        elif rsi < 30 and self.df["close"][-1] > min(self.df["close"][-n:]) and self.buy == False:
            print("매수합니다")
            info = info + "매수합니다."
            self.count += 1
        else:
            print("매매하지 않습니다")
            info = info + "매매하지 않습니다."

    def trade(self):
        global info
        if self.count == 1:
            if self.upbit.get_balance("KRW") > self.spend:
                if self.buy == False:
                    self.buy = True
                    self.buyPrice = pyupbit.get_current_price(self.coin)
                    self.upbit.buy_market_order(self.coin, self.spend)
                    print(f"매수합니다\n매수금액: {self.spend}")
                    info = info + "매수합니다.\n매수금액: " + str(self.spend)
                else:
                    self.buy = False
                    currPrice = pyupbit.get_current_price(self.coin)
                    if self.buyPrice == 0:
                        sellPrice = self.spend
                    else:
                        sellPrice = self.spend * (self.buyPrice / currPrice) * (1 / 0.9995)
                    self.upbit.sell_market_order(self.coin, sellPrice / currPrice)
                    print(f"매도합니다\n매도금액: {sellPrice}")
                    info = info + "매도합니다\n매도금액: " + str(sellPrice)
            else:
                print("잔액이 부족합니다")
                info = info + "잔액이 부족합니다."
        else:
            print("매매하지 않습니다")
            info = info + "매매하지 않습니다."
