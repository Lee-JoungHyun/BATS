from rest_framework import serializers
from .models import Account


class AccountSerializer(serializers.ModelSerializer):
    class Meta:
        model = Account
        fields = ['name', 'id', 'key', 'password', 'email']


class LoginSerializer(serializers.ModelSerializer):
    class Meta:
        model = Account
        fields = ['id', 'password']



