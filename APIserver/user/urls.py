from django.urls import path
from .views import hello_api, hello_api, sign_up, login, auto_trade


urlpatterns = [
    path('', hello_api),
    path('sign_up/', sign_up),
    path('login/', login),
    path('logined/trade/', auto_trade),
]