import React from 'react';

import 'react-native-gesture-handler'
import { createStackNavigator } from '@react-navigation/stack';

import MainPage from '../Pages/MainPage';
import DetailPage from '../Pages/DetailPage';

const Stack = createStackNavigator();

function StackNavigator() {
    return (
        <Stack.Navigator ScreenOptions={{
            headerStyle: {
                backroundColor: '#d4531e',
                borderBottomColor: "#d3d3d3",
                height: 80
            },
            headerTintColor: '#fff',
            headerBackTitleVisible: false
        }}
        >
            <Stack.Screen name="MainPage" 
                options={{
                    title: '꿀팁'
                }}
                component={MainPage}/>
            <Stack.Screen name="DetailPage" 
                options={{
                    title: 'Detail'
                }}
                component={DetailPage}/>

        </Stack.Navigator>
    )
}

export default StackNavigator;