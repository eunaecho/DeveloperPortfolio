import React from 'react';

import 'react-native-gesture-handler'
import { createStackNavigator } from '@react-navigation/stack';

import MainPage from '../Pages/MainPage';
import DetailPage from '../Pages/DetailPage';


const Stack = createStackNavigator();

function StackNavigator() {
    return (
        <Stack.Navigator 
            screenOptions={{
                headerStyle: {
                    backgroundColor: '#00a86b',
                    height: 100
                },
                headerTintColor: '#fff',
                headerBackTitleVisible: false,
                
        }}
        >   
            <Stack.Screen name="MainPage" 
                options={{
                    title: '세상의 모든 팁',
                    animationTypeForReplace: 'pop',
                    headerTitleAlign: 'left',
                    
                }}
                component={MainPage}/>
            <Stack.Screen name="DetailPage" 
                options={{
                    title: '상세 내용'
                }}
                component={DetailPage}/>

        </Stack.Navigator>
    )
}

export default StackNavigator;